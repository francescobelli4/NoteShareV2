package app.views.fx.graphiccontrollers.auth;

import app.appcontrollers.LoginController;
import app.exceptions.AuthException;
import app.views.GraphicController;
import permissions.Role;

import java.util.Objects;

public class FxAuthRegisterViewController implements GraphicController {

    private final LoginController loginController;

    public FxAuthRegisterViewController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void registerButtonClicked(String username, String password, String confirmPassword, String role) throws AuthException {

        if (!Objects.equals(confirmPassword, password)) {
            throw new AuthException("Passwords do not match");
        }

        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank() || role.isBlank()) {
            throw new AuthException("One or more fields are empty");
        }

        Role parsedRole;
        if (role.equals("Student")) {
            parsedRole = Role.STUDENT;
        } else {
            parsedRole = Role.TEACHER;
        }

        loginController.performRegister(username, password, parsedRole);
    }
}
