package app;


import app.config.AppConfig;
import app.config.GuiType;
import app.models.Models;
import app.models.usermodel.UserModel;
import app.network.ConnectionManager;
import app.views.fx.FxLauncher;

import java.io.IOException;
import java.util.logging.Logger;

public class App {

    public static AppConfig APPCONFIG;
    private static final Logger LOGGER = Logger.getLogger("App");

    static void main(String[] args) {

        LOGGER.info("Starting NoteShare...");

        try {
            APPCONFIG = AppConfig.create(args);
        } catch (IllegalArgumentException e) {

            LOGGER.severe(() -> String.format("NoteShare terminated for: %n%s", e.getMessage()));
            System.exit(1);
        }

        LOGGER.info(() -> String.format("App config set: %s %s %s",
                APPCONFIG.mode(),
                APPCONFIG.guiType(),
                APPCONFIG.locale()
        ));

        try {
            ConnectionManager.initializeConnection("localhost", 12345);
        } catch (IOException e) {

            LOGGER.severe(() -> String.format("NoteShare failed to connect to server: %n%s", e.getMessage()));
            System.exit(1);
        }

        Models models = new Models(new UserModel());

        if (APPCONFIG.guiType() == GuiType.FX) {
            FxLauncher.launchApp(models);
        }
    }
}
