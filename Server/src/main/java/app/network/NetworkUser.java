package app.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class NetworkUser implements Runnable {

    private final Logger logger;

    private final Socket clientSocket;
    private final String address;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public NetworkUser(Socket clientSocket) {
        this.clientSocket = clientSocket;
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
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
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
     * This function should actually read the data from the input stream and call MessageHandler
     * to handle the message.
     */
    private void read() {
        try {
            String data = dataInputStream.readUTF();
        } catch (IOException ioException) {
            logger.severe(String.format("Connection with client (%s) lost: %s", address, ioException.getMessage()));
            destroy();
        } catch (NullPointerException _) {
            destroy();
        }
    }

    /**
     * This function should destroy this thread, the associated client and its structures
     * and data.
     */
    private void destroy() {
        logger.info(() -> String.format("Destroying client (%s).", address));

        if (dataInputStream != null) {

            try {
                dataInputStream.close();
            } catch (IOException _) {
                // Not needed
            }
            dataInputStream = null;
        }

        if (dataOutputStream != null) {

            try {
                dataOutputStream.close();
            } catch (IOException _) {
                // Not needed
            }
            dataOutputStream = null;
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
