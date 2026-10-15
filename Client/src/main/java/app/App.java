package app;


import app.config.AppConfig;

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

        Launcher.launchApp();

    }
}
