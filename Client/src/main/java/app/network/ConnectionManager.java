package app.network;

import dtos.Message;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ConnectionManager {

    private static final Logger LOGGER = Logger.getLogger("Connection Manager");

    private static Socket serverSocket;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

    private static final ConcurrentHashMap<String, CompletableFuture<Message>> pendingRequests = new ConcurrentHashMap<>();

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
            executorService.submit(() -> handleIncomingData(message));
        } catch (IOException e) {
            LOGGER.severe(() -> String.format("Connection with server lost: %s", e.getMessage()));
            closeConnection();
        } catch (ClassNotFoundException e) {
            LOGGER.severe(() -> String.format("Failed de-serializing an object: %s", e.getMessage()));
        } catch (NullPointerException _) {
            closeConnection();
        }
    }

    private static void handleIncomingData(Message message) {

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
