package app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {

    private static final Logger LOGGER = Logger.getLogger("Connection Manager");

    private int port;
    private ServerSocket serverSocket;

    public ConnectionManager(int port) {
        this.port = port;
    }

    public void initializeServerSocket() throws IOException {

        serverSocket = new ServerSocket(port);

        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.info(String.format("Server socket initialized and listening on port %d!", port));

        while (!serverSocket.isClosed()) {

            Socket clientSocket;

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                LOGGER.warning(String.format("A client could not connect to the server: %s. Skipping...", e.getMessage()));
                continue;
            }

            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.info(String.format("A new client (%s) connected!", clientSocket.getInetAddress().getHostAddress()));

            //Thread t = new Thread(new NetworkUser(client));
            //t.start();
        }

        LOGGER.severe("Server socket is closed!");
        System.exit(-1);
    }
}
