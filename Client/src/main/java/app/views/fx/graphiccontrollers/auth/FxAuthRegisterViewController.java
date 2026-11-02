package app.views.fx.graphiccontrollers.auth;

import app.appcontrollers.AuthController;
import app.beans.RegisterBean;
import app.exceptions.AuthException;
import app.views.GraphicController;
import permissions.Role;
import utils.AuthRules;
import utils.AuthServiceError;
import utils.PasswordError;
import utils.UsernameError;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FxAuthRegisterViewController implements GraphicController {

    private final AuthController authController;

    public FxAuthRegisterViewController(AuthController authController) {
        this.authController = authController;
    }

    public CompletableFuture<Void> registerButtonClicked(String username, String password, String confirmPassword, String role) throws AuthException {

        Optional<UsernameError> usernameError = AuthRules.isUsernameValid(username);

        if (usernameError.isPresent()) {
            throw new AuthException(usernameError.get());
        }

        Optional<PasswordError> passwordError = AuthRules.isPasswordValid(password);

        if (passwordError.isPresent()) {
            throw new AuthException(passwordError.get());
        }

        if (!Objects.equals(confirmPassword, password)) {
            throw new AuthException(AuthServiceError.PASSWORDS_DO_NOT_MATCH);
        }

        if (role == null || role.isBlank()) {
            throw new AuthException(AuthServiceError.INVALID_ROLE);
        }

        Role parsedRole = switch (role) {
            case "Student"   -> Role.STUDENT;
            case "Teacher" -> Role.TEACHER;
            default           -> throw new AuthException(AuthServiceError.INVALID_ROLE);
        };

        return authController.performRegister(new RegisterBean(username, password, parsedRole));
    }
}
