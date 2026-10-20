package app;


import app.config.AppConfig;
import app.config.GuiType;
import app.views.fx.FxLauncher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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

        if (APPCONFIG.guiType() == GuiType.FX) {

            try {
                Socket socket = new Socket("127.0.0.1", 12345);

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {

            }
            FxLauncher.launchApp();
        }
    }
}
