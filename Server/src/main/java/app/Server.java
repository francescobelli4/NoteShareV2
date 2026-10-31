package app;

import app.config.AppConfig;
import app.config.Mode;
import app.daos.DAOs;
import app.daos.user.DemoUserDAO;
import app.daos.user.UserDAO;
import app.network.ConnectionManager;
import app.network.MessageHandler;
import app.network.NetworkUser;
import app.services.AuthService;
import app.services.Services;

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

        LOGGER.info(() -> String.format("App config set: %s", APPCONFIG.mode()));

        UserDAO userDAO = APPCONFIG.mode() == Mode.DEMO ? new DemoUserDAO() : null;
        DAOs daos = new DAOs(userDAO);

        AuthService authService = new AuthService(daos.userDAO());
        Services services = new Services(authService);

        MessageHandler messageHandler = new MessageHandler(services);

        ConnectionManager connectionManager = new ConnectionManager(
                12345,
                clientSocket -> {
                    Thread t = new Thread(new NetworkUser(clientSocket, messageHandler));
                    t.setDaemon(true);
                    t.start();
                }
        );

        try {
            connectionManager.initializeServerSocket();
            connectionManager.acceptClients();
        } catch (IOException e) {
            LOGGER.severe(() -> String.format("NoteShare Server terminated for: %n%s", e.getMessage()));
            System.exit(1);
        }
    }

    static void main(String[] args) { initializeServer(args); }
}
