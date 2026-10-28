package app.appcontrollers;

import app.beans.LoginBean;
import app.beans.RegisterBean;
import app.exceptions.AuthException;
import app.models.usermodel.UserModel;
import app.network.ConnectionManager;
import dtos.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class AuthController {

    private final UserModel userModel;

    public AuthController(UserModel userModel) {
        this.userModel = userModel;
    }

    public CompletableFuture<Void> performLogin(LoginBean loginBean) {
        LoginRequestDTO request = new LoginRequestDTO(
                ConnectionManager.nextRequestId(),
                loginBean.username(),
                loginBean.password()
        );

        // In order to update the error message in the UI (in case, for example, username is already in use), we should
        // return the CompletionStage returned by thenAccept to the view, that will be able to manage the AuthException
        // or the IllegalStateException
        return ConnectionManager.sendAsync(request).orTimeout(10, TimeUnit.SECONDS)
                .thenAccept(resp -> {
                    switch (resp) {
                        case LoginResponseDTO success -> userModel.logIn(success.username(), success.role());
                        case ErrorResponseDTO error      -> throw new AuthException(error.error());
                        default                          -> throw new IllegalStateException("Unexpected response");
                    }
                });
    }

    public CompletableFuture<Void> performRegister(RegisterBean registerBean) {

        RegisterRequestDTO request = new RegisterRequestDTO(
                ConnectionManager.nextRequestId(),
                registerBean.username(),
                registerBean.password(),
                registerBean.role()
        );

        // In order to update the error message in the UI (in case, for example, username is already in use), we should
        // return the CompletionStage returned by thenAccept to the view, that will be able to manage the AuthException
        // or the IllegalStateException
        return ConnectionManager.sendAsync(request).orTimeout(10, TimeUnit.SECONDS)
                .thenAccept(resp -> {
                    switch (resp) {
                        case RegisterResponseDTO success -> userModel.logIn(success.username(), success.role());
                        case ErrorResponseDTO error      -> throw new AuthException(error.error());
                        default                          -> throw new IllegalStateException("Unexpected response");
                    }
                });
    }
}
