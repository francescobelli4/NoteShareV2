package app.views.fx.graphiccontrollers.home;

import app.appcontrollers.AuthController;
import app.appcontrollers.ExplorerController;
import app.beans.LoginUsingTokenBean;
import app.models.explorermodel.ExplorerMode;
import app.utils.TokenManager;
import app.views.GraphicController;
import app.views.fx.FxNavigator;
import app.views.fx.FxViewFactory;

import java.io.IOException;

public class FxHomeViewController implements GraphicController {

    private final FxNavigator navigator;
    private final FxViewFactory factory;
    private final AuthController authController;
    private final ExplorerController explorerController;

    public FxHomeViewController(FxNavigator navigator, FxViewFactory factory, AuthController authController, ExplorerController explorerController) {
        this.navigator = navigator;
        this.factory = factory;
        this.authController = authController;
        this.explorerController = explorerController;
    }

    public void onLoginButtonClicked() {
        navigator.push(factory.createFxAuthView());
    }

    public void tryLoginUsingToken() {
        try {
            String token = TokenManager.getToken();
            if (token == null) return;

            authController.performLoginUsingToken(new LoginUsingTokenBean(token));
        } catch (IOException _) {
            // We should just ignore it if token was not found
        }
    }

    public void onYourNotesButtonClicked() {
        explorerController.setExplorerMode(ExplorerMode.LIBRARY);
    }

    public void onExploreButtonClicked() {
        explorerController.setExplorerMode(ExplorerMode.STORE);
    }
}
