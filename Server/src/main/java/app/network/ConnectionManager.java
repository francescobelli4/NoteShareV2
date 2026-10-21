package app.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionManager {

    private static final Logger LOGGER = Logger.getLogger("Connection Manager");

    private final int port;

    public ConnectionManager(int port) {
        this.port = port;
    }

    /**
     * This function should initialize the connection with the server
     * @throws IOException could not connect to the server
     */
    public void initializeServerSocket() throws IOException {

        ServerSocket serverSocket = new ServerSocket(port);

        LOGGER.info(() -> String.format("Server socket initialized and listening on port %d!", port));

        while (!serverSocket.isClosed()) {

            Socket clientSocket;

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                LOGGER.warning(String.format("A client could not connect to the server: %s. Skipping...", e.getMessage()));
                continue;
            }


            LOGGER.info(() -> String.format("A new client (%s) connected!", clientSocket.getInetAddress().getHostAddress()));


            Thread t = new Thread(new NetworkUser(clientSocket));
            t.start();
        }

        LOGGER.severe("Server socket is closed!");
        System.exit(-1);
    }
}
