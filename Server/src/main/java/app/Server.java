package app;

import app.config.AppConfig;
import app.network.ConnectionManager;

import java.io.IOException;
import java.util.logging.Logger;

public class Server {

    public static AppConfig APPCONFIG;
    private static final Logger LOGGER = Logger.getLogger("Server");

    private Server() {
        // Nothing to do for now...
    }

    private static void initializeServer(String[] args) {

        LOGGER.info("Starting NoteShare Server...");

        try {
            APPCONFIG = AppConfig.create(args);
        } catch (IllegalArgumentException e) {

            LOGGER.severe(() -> String.format("NoteShare Server terminated for: %n%s", e.getMessage()));
            System.exit(1);
        }

        LOGGER.info(() -> String.format("App config set: %s",
                APPCONFIG.mode()
        ));

        ConnectionManager connectionManager = new ConnectionManager(12345);

        try {
            connectionManager.initializeServerSocket();
        } catch (IOException e) {
            LOGGER.severe(() -> String.format("NoteShare Server terminated for: %n%s", e.getMessage()));
            System.exit(1);
        }
    }

    static void main(String[] args) { initializeServer(args); }
}
