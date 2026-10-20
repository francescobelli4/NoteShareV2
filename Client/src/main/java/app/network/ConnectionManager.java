package app.network;

import dtos.Message;
import dtos.Request;
import dtos.Response;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {

    private static final Logger LOGGER = Logger.getLogger("Connection Manager");

    private static Socket serverSocket;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

    private static final ConcurrentHashMap<Long, CompletableFuture<Response>> pendingRequests = new ConcurrentHashMap<>();

    private static final int MAX_PARALLEL_THREADS = 10;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(MAX_PARALLEL_THREADS);
    private static final Object writeLock = new Object();

    private ConnectionManager() {
        // Nothing to do
    }

    public static void initializeConnection(String host, int port) throws IOException {
        serverSocket = new Socket(host, port);

        objectInputStream = new ObjectInputStream(serverSocket.getInputStream());
        objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());

        startReaderThread();
    }

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

    private static void handleIncomingMessage(Message message) {

        if (!(message instanceof Response response)) {
            LOGGER.severe(() -> String.format("Unexpected message: %s", message.getClass().getSimpleName()));
            return;
        }

        handleResponse(response);
    }

    private static void handleResponse(Response response) {
        CompletableFuture<Response> future = pendingRequests.remove(response.requestId());

        if (future != null) {
            future.complete(response);
        } else {
            // Didn't find a future for this response
        }
    }

    private static CompletableFuture<Response> sendAsync(Request request) {

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

    private static Response sendSync(Request request) throws IOException, InterruptedException, ExecutionException{

        CompletableFuture<Response> future = new CompletableFuture<>();

        try {

            synchronized (writeLock) {
                pendingRequests.put(request.requestId(), future);
                if (LOGGER.isLoggable(Level.INFO))
                    LOGGER.info(String.format("SENDING %s", request));
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

    private static void closeConnection() {
        try {

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

            while (true) {

                assert serverSocket != null;
                read();
            }
        });

        t.start();
    }
}
