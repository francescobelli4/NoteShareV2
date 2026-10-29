package app.views.fx.graphiccontrollers.auth;

import app.appcontrollers.AuthController;
import app.beans.LoginBean;
import app.exceptions.AuthException;
import app.views.GraphicController;
import utils.AuthRules;
import utils.PasswordError;
import utils.UsernameError;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FxAuthLoginViewController implements GraphicController {

    private final AuthController authController;

    public FxAuthLoginViewController(AuthController authController) {
        this.authController = authController;
    }

    public CompletableFuture<Void> onLoginButtonClicked(String username, String password) throws AuthException {

        Optional<UsernameError> usernameError = AuthRules.isUsernameValid(username);

        if (usernameError.isPresent()) {
            throw new AuthException(usernameError.get());
        }

        Optional<PasswordError> passwordError = AuthRules.isPasswordValid(password);

        if (passwordError.isPresent()) {
            throw new AuthException(passwordError.get());
        }

        return authController.performLogin(new LoginBean(username, password));
    }
}
