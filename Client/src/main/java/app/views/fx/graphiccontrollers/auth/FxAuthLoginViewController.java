package app.views.fx.graphiccontrollers.auth;

import app.appcontrollers.AuthController;
import app.beans.LoginBean;
import app.exceptions.AuthException;
import app.views.GraphicController;
import utils.AuthRules;
import utils.PasswordError;
import utils.UsernameError;

import java.util.Optional;

public class FxAuthLoginViewController implements GraphicController {

    private final AuthController authController;

    public FxAuthLoginViewController(AuthController authController) {
        this.authController = authController;
    }

    public void onLoginButtonClicked(String username, String password) throws AuthException {

        Optional<UsernameError> usernameError = AuthRules.isUsernameValid(username);

        if (usernameError.isPresent()) {
            switch (usernameError.get()) {
                case TOO_LONG -> throw new AuthException("Username is too long");
                case TOO_SHORT -> throw new AuthException("Username is too short");
                case NULL_OR_EMPTY -> throw new AuthException("Username is empty");
                case NOT_ALPHANUMERIC -> throw new AuthException("Username is not alphanumeric");
            }
        }

        Optional<PasswordError> passwordError = AuthRules.isPasswordValid(password);

        if (passwordError.isPresent()) {
            switch (passwordError.get()) {
                case TOO_LONG -> throw new AuthException("Password is too long");
                case TOO_SHORT -> throw new AuthException("Password is too short");
                case NULL_OR_EMPTY -> throw new AuthException("Password is empty");
                case NOT_ALPHANUMERIC -> throw new AuthException("Password is not alphanumeric");
            }
        }

        authController.performLogin(new LoginBean(username, password));
    }
}
