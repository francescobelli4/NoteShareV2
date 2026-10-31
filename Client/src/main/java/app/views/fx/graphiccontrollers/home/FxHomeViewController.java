package app.views.fx.graphiccontrollers.home;

import app.appcontrollers.AuthController;
import app.beans.LoginUsingTokenBean;
import app.utils.TokenManager;
import app.views.GraphicController;
import app.views.fx.FxNavigator;
import app.views.fx.FxViewFactory;

import java.io.IOException;

public class FxHomeViewController implements GraphicController {

    private final FxNavigator navigator;
    private final FxViewFactory factory;
    private final AuthController authController;

    public FxHomeViewController(FxNavigator navigator, FxViewFactory factory, AuthController authController) {
        this.navigator = navigator;
        this.factory = factory;
        this.authController = authController;
    }

    public void onLoginButtonClicked() {
        navigator.push(factory.createFxAuthView());
    }

    public void tryLoginUsingToken() {
        try {
            String token = TokenManager.getToken();
            if (token == null) return;

            authController.performLoginUsingToken(new LoginUsingTokenBean(token));
        } catch (IOException e) {
            // We should just ignore it if token was not found
        }
    }
}
