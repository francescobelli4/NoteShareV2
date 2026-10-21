package app.network;

import dtos.Message;
import dtos.Request;
import dtos.Response;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ConnectionManager {

    private static final Logger LOGGER = Logger.getLogger("Connection Manager");

    private static Socket serverSocket;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

    private static final ConcurrentHashMap<Long, CompletableFuture<Response>> pendingRequests = new ConcurrentHashMap<>();

    private static final int MAX_PARALLEL_THREADS = 10;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(MAX_PARALLEL_THREADS, (r) -> {
        Thread t = new Thread(r);
        // Making sure every thread is a daemon thread
        t.setDaemon(true);
        return t;
    });
    private static final Object writeLock = new Object();

    private ConnectionManager() {
        // Nothing to do
    }

    public static void initializeConnection(String host, int port) throws IOException {
        serverSocket = new Socket(host, port);

        objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());
        objectInputStream = new ObjectInputStream(serverSocket.getInputStream());

        startReaderThread();
    }

    /**
     * This function should read a Message object
     */
    private static void read() {

        try {
            Message message = (Message) objectInputStream.readObject();
            executorService.submit(() -> handleIncomingMessage(message));
        } catch (IOException e) {
            LOGGER.severe(() -> String.format("Connection with server lost: %s", e.getMessage()));
            closeConnection();
        } catch (ClassNotFoundException e) {
            LOGGER.severe(() -> String.format("Failed de-serializing an object: %s", e.getMessage()));
        } catch (NullPointerException _) {
            closeConnection();
        }
    }

    /**
     * This function should handle the incoming data.
     *
     * RESPONSE:     a Response message surely (if there are no unexpected errors) has a CompletableFuture
     *               waiting for that response. The handling logic should not be in this class, so I only
     *               notify the CompletableFuture that the response has arrived.
     *
     * NOTIFICATION (Still to do): a Notification message does not have any CompletableFuture waiting for it. The handling
     *               is based on the SocketMessageType.
     *
     * @param message the received Message object
     */
    private static void handleIncomingMessage(Message message) {

        if (!(message instanceof Response response)) {
            LOGGER.severe(() -> String.format("Unexpected message: %s", message.getClass().getSimpleName()));
            return;
        }

        handleResponse(response);
    }

    /**
     * Handling the Response object received from the server.
     * @param response the Response object
     */
    private static void handleResponse(Response response) {
        CompletableFuture<Response> future = pendingRequests.remove(response.requestId());

        if (future != null) {
            future.complete(response);
        } else {
            LOGGER.warning(() -> String.format("Could not find a future for that id: %d", response.requestId()));
        }
    }

    /**
     * This function should send an async request to the server.
     * An async request awaits an answer from the server, but it does not block the thread.
     * Even if this function should be quite fast, it's suggested to avoid running it in the
     * UI thread.
     *
     * @param request the request that has to be sent
     * @return a CompletableFuture<Response> that will be notified when the answer arrives
     */
    public static CompletableFuture<Response> sendAsync(Request request) {

        CompletableFuture<Response> future = new CompletableFuture<>();

        executorService.submit(() -> {

            try {

                synchronized (writeLock) {

                    pendingRequests.put(request.requestId(), future);
                    objectOutputStream.writeObject(request);
                    objectOutputStream.flush();
                }
            } catch (IOException ioException) {
                if (pendingRequests.containsKey(request.requestId()))
                    pendingRequests.remove(request.requestId()).completeExceptionally(ioException);
            }
        });

        return future;
    }

    /**
     * This function should send a sync request to the server.
     * A sync request blocks the running thread until the answer is received from
     * the server.
     *
     * @param request the request that has to be sent
     * @return the Response object received as response
     */
    public static Response sendSync(Request request) throws IOException, InterruptedException, ExecutionException{

        CompletableFuture<Response> future = new CompletableFuture<>();

        try {

            synchronized (writeLock) {

                pendingRequests.put(request.requestId(), future);
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();
            }

            return future.get();
        } catch (IOException | InterruptedException | ExecutionException ex) {
            if (pendingRequests.containsKey(request.requestId()))
                pendingRequests.remove(request.requestId()).completeExceptionally(ex);
            throw ex;
        }
    }

    public static void closeConnection() {
        try {
            // Making sure every pending request gets terminated with an exception if connection is closed
            pendingRequests.values().forEach(f ->
                    f.completeExceptionally(new IOException("Connection closed"))
            );
            pendingRequests.clear();

            if (objectInputStream != null) {
                objectInputStream.close();
            }

            if (objectOutputStream != null) {
                objectOutputStream.close();
            }

            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LOGGER.warning(String.format("Error during resource cleanup: %s", e.getMessage()));
        }
    }

    private static void startReaderThread() {

        Thread t = new Thread(() -> {

            while (!serverSocket.isClosed()) {

                assert serverSocket != null;
                read();
            }
        });

        // Daemon threads get closed when all the user threads terminated
        t.setDaemon(true);
        t.start();
    }
}
