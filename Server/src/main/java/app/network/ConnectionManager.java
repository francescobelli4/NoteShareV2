package app.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class ConnectionManager {

    private static final Logger LOGGER = Logger.getLogger("Connection Manager");

    private int port;
    private final Consumer<Socket> clientAccepted;

    private ServerSocket serverSocket;

    public ConnectionManager(int port, Consumer<Socket> clientAccepted) {
        this.port = port;
        this.clientAccepted = clientAccepted;
    }

    /**
     * This function should initialize the connection with the server
     * @throws IOException could not connect to the server
     */
    public void initializeServerSocket() throws IOException {
        serverSocket = new ServerSocket(port);
        port = serverSocket.getLocalPort();
        LOGGER.info(() -> String.format("Server socket initialized on port %d!", port));
    }

    /**
     * This function should start the cycle that accepts clients and executes clientAccepted consumer.
     */
    public void acceptClients() {

        while (!serverSocket.isClosed()) {

            try {

                Socket clientSocket = serverSocket.accept();
                LOGGER.info(() -> String.format("A new client (%s) connected!", clientSocket.getInetAddress().getHostAddress()));

                clientAccepted.accept(clientSocket);
            } catch (IOException e) {

                if (serverSocket.isClosed()) break;

                LOGGER.warning(() -> String.format("A client could not connect to the server: %s. Skipping...", e.getMessage()));
            }
        }

        LOGGER.info("Server socket closed.");
    }

    /* TESTING */
    public int getPort() {
        return port;
    }

    public void closeServerSocket() throws IOException {
        serverSocket.close();
    }
}
