package app.network;

import dtos.LoginRequestDTO;
import dtos.Message;
import dtos.Request;

import java.util.logging.Logger;

public class MessageHandler {

    private static final Logger LOGGER = Logger.getLogger("MessageHandler");

    private MessageHandler() {
        // Nothing to do
    }

    public static void handleMessage(Message message) {

        if (!(message instanceof Request)) {
            LOGGER.severe(() -> String.format("Unexpected message: %s", message.getClass().getSimpleName()));
            return;
        }

        Request request = (Request) message;

        switch (request) {
            case LoginRequestDTO loginReq -> {
                //Handle login request :D
            }
        }
    }
}
