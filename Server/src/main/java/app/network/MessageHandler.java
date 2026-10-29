package app.network;

import app.services.Services;
import dtos.LoginRequestDTO;
import dtos.Message;
import dtos.RegisterRequestDTO;
import dtos.Request;

import java.util.logging.Logger;

public class MessageHandler {

    private static final Logger LOGGER = Logger.getLogger("MessageHandler");

    private final Services services;

    public MessageHandler(Services services) {
        this.services = services;
    }

    public void handleMessage(Message message) {

        if (!(message instanceof Request request)) {
            LOGGER.severe(() -> String.format("Unexpected message: %s", message.getClass().getSimpleName()));
            return;
        }

        switch (request) {
            case LoginRequestDTO loginReq -> services.authService().performLogin(loginReq);
            case RegisterRequestDTO registerReq -> services.authService().performRegister(registerReq);
        }
    }
}
