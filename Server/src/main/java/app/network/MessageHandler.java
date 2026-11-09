package app.network;

import app.exceptions.AuthServiceException;
import app.exceptions.ProfileServiceException;
import app.services.Services;
import dtos.*;

import java.util.logging.Logger;

public class MessageHandler {

    private static final Logger LOGGER = Logger.getLogger("MessageHandler");

    private final Services services;

    public MessageHandler(Services services) {
        this.services = services;
    }

    /**
     * This function should handle the client's request and return a response.
     * A failure response is always a ErrorResponseDTO, which specifies the ErrorCode for that specific error.
     * @param message the received object. It should be handled only if it's a Request object.
     * @return a response that will be sent to the client.
     */
    public Response handleMessage(Message message, Session userSession) {

        if (!(message instanceof Request request)) {
            LOGGER.severe(() -> String.format("Unexpected message: %s", message.getClass().getSimpleName()));
            return null;
        }

        LOGGER.info(() -> String.format("Received %s", message));

        switch (request) {
            case LoginRequestDTO loginReq -> {

                try {
                    return services.authService().performLogin(loginReq, userSession);
                } catch (AuthServiceException e) {
                    return new ErrorResponseDTO(loginReq.requestId(), e.getAuthError());
                }
            }
            case RegisterRequestDTO registerReq -> {

                try {
                    return services.authService().performRegister(registerReq, userSession);
                } catch (AuthServiceException e) {
                    return new ErrorResponseDTO(registerReq.requestId(), e.getAuthError());
                }
            }
            case LoginUsingTokenRequestDTO loginUsingTokenReq -> {
                try {
                    return services.authService().performLoginUsingToken(loginUsingTokenReq, userSession);
                } catch (AuthServiceException e) {
                    return new ErrorResponseDTO(loginUsingTokenReq.requestId(), e.getAuthError());
                }
            }
            case GetWalletBalanceRequestDTO getWalletBalanceRequestDTO -> {
                try {
                    return services.walletService().getWalletBalance(getWalletBalanceRequestDTO, userSession);
                } catch (ProfileServiceException e) {
                    return new ErrorResponseDTO(getWalletBalanceRequestDTO.requestId(), e.getProfileError());
                }
            }
        }
    }
}
