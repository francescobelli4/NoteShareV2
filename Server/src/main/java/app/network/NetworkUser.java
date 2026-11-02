package app.network;

import dtos.Message;
import dtos.Response;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class NetworkUser implements Runnable {

    private final Logger logger;

    private final Socket clientSocket;
    private final MessageHandler messageHandler;

    private SafeObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private final String address;

    public NetworkUser(Socket clientSocket, MessageHandler messageHandler) {
        this.clientSocket = clientSocket;
        this.messageHandler = messageHandler;

        this.address = clientSocket.getInetAddress().getHostAddress();
        this.logger = Logger.getLogger(String.format("Client (%s)", this.address));
    }

    /**
     * This function should set up the communication with the user and start listening for
     * messages.
     */
    @Override
    public void run() {

        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new SafeObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            logger.severe(() -> String.format("Could not get client (%s) input/output stream: %s. Closing connection.", address, e.getMessage()));
            destroy();
            return;
        }

        while (!clientSocket.isClosed()) {
            read();
        }

        destroy();
    }

    /**
     * This function should actually read the data from the input stream and delegate the message handling to
     * MessageHandler class.
     * MessageHandler always generates a response that has to be sent immediately to the client.
     */
    private void read() {
        try {

            Message data = (Message) objectInputStream.readObject();
            Response response = messageHandler.handleMessage(data);
            write(response);
        } catch (IOException ioException) {
            logger.severe(() -> String.format("Connection with client (%s) lost: %s", address, ioException.getMessage()));
            destroy();
        } catch (ClassNotFoundException e) {
            logger.severe(() -> String.format("Failed de-serializing an object: %s", e.getMessage()));
        } catch (NullPointerException _) {
            destroy();
        }
    }

    /**
     * This function should send a message to the client.
     * This function is NOT thread-safe! Only this thread should write on the output stream!
     * @param response the message that should be sent to the client.
     */
    private void write(Response response) {

        try {
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        } catch (IOException e) {
            logger.severe(String.format("Connection with client (%s) lost: %s.", address, e.getMessage()));
            destroy();
        }
    }

    /**
     * This function should destroy this thread, the associated client and its structures
     * and data.
     */
    private void destroy() {
        logger.info(() -> String.format("Destroying client (%s).", address));

        if (objectInputStream != null) {

            try {
                objectInputStream.close();
            } catch (IOException _) {
                // Not needed
            }
            objectInputStream = null;
        }

        if (objectOutputStream != null) {

            try {
                objectOutputStream.close();
            } catch (IOException _) {
                // Not needed
            }
            objectOutputStream = null;
        }

        if (clientSocket != null && clientSocket.isConnected()) {
            try {
                clientSocket.close();
            } catch (IOException _){
                // Not needed
            }
        }

        Thread.currentThread().interrupt();
    }
}
