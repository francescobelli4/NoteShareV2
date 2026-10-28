package app.views.fx.graphiccontrollers.auth;

import app.appcontrollers.AuthController;
import app.beans.RegisterBean;
import app.exceptions.AuthException;
import app.views.GraphicController;
import permissions.Role;
import utils.AuthRules;
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

    public void registerButtonClicked(String username, String password, String confirmPassword, String role) throws AuthException {

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

        if (!Objects.equals(confirmPassword, password)) {
            throw new AuthException("Passwords do not match");
        }

        if (role == null || role.isBlank()) {
            throw new AuthException("Role is blank");
        }

        Role parsedRole = switch (role) {
            case "Student"   -> Role.STUDENT;
            case "Teacher" -> Role.TEACHER;
            default           -> throw new AuthException("Unknown role");
        };

        authController.performRegister(new RegisterBean(username, password, parsedRole)).exceptionally(error -> {
            throw new AuthException(error.getMessage());
        });
    }
}
