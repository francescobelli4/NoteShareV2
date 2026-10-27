package app.views.fx.graphiccontrollers.auth;

import app.appcontrollers.LoginController;
import app.exceptions.AuthException;
import app.views.GraphicController;

public class FxAuthLoginViewController implements GraphicController {

    private final LoginController loginController;

    public FxAuthLoginViewController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void onLoginButtonClicked(String username, String password) throws AuthException {

        if (username.isBlank() || password.isBlank()) {
            throw new AuthException("One or more fields are empty");
        }

        loginController.performLogin(username, password);
    }
}
