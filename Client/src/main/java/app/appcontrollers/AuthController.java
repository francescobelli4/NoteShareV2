package app.appcontrollers;

import app.beans.LoginBean;
import app.beans.LoginUsingTokenBean;
import app.beans.RegisterBean;
import app.exceptions.AuthException;
import app.models.usermodel.UserModel;
import app.network.ConnectionManager;
import app.utils.TokenManager;
import dtos.*;
import utils.AuthError;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class is an application controller that manages the "Login" use case.
 */
public class AuthController {

    private final UserModel userModel;

    public AuthController(UserModel userModel) {
        this.userModel = userModel;
    }

    /**
     * This function should send an async request to the server to login.
     * So, we build the LoginRequestDTO and then we sendAsync this request to the server: when the server answers,
     * ConnectionManager calls future.complete(response), the lambda specified in .thenAccept(lambda) is executed.
     * Note that .thenAccept(lambda) is a CompletableFuture<Void> that is returned to the view: in case of
     * AuthException or IllegaStateException, the .exceptionally(lambda) block in the view is executed.
     * Obviously this function does not throw AuthException or IllegalStateException because they are thrown in the
     * async block!
     * @param loginBean the user data
     * @return a CompletableFuture<Void> used by the view to show the AuthException to the user.
     */
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
                        case LoginResponseDTO success -> {
                            TokenManager.saveToken(success.token());
                            userModel.logIn(success.username(), success.role());
                        }
                        case ErrorResponseDTO error      -> throw new AuthException((AuthError) error.error());
                        default                          -> throw new IllegalStateException("Unexpected response");
                    }
                });
    }

    /**
     * This function should send an async request to the server to login.
     * The logic is the same of performLogin function! :D
     * @param registerBean the user data
     * @return a CompletableFuture<Void> used by the view to show the AuthException to the user.
     */
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
                        case RegisterResponseDTO success -> {
                            TokenManager.saveToken(success.token());
                            userModel.logIn(success.username(), success.role());
                        }
                        case ErrorResponseDTO error      -> throw new AuthException((AuthError) error.error());
                        default                          -> throw new IllegalStateException("Unexpected response");
                    }
                });
    }


    /**
     * This function should send an async request to the server to perform login using user token, if he has one.
     * @param loginUsingTokenBean the user data
     */
    public void performLoginUsingToken(LoginUsingTokenBean loginUsingTokenBean) {

        LoginUsingTokenRequestDTO request = new LoginUsingTokenRequestDTO(
                ConnectionManager.nextRequestId(),
                loginUsingTokenBean.token()
        );

        ConnectionManager.sendAsync(request).orTimeout(10, TimeUnit.SECONDS).thenAccept(resp -> {
            LoginUsingTokenResponseDTO success = (LoginUsingTokenResponseDTO) resp;
            TokenManager.saveToken(success.token());
            userModel.logIn(success.username(), success.role());
        });
    }
}
