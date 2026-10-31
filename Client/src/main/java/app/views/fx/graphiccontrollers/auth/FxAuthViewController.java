package app.views.fx.graphiccontrollers.auth;

import app.views.GraphicController;
import app.views.fx.FxNavigator;

public class FxAuthViewController implements GraphicController {

    private final FxNavigator navigator;

    public FxAuthViewController(FxNavigator navigator) {
        this.navigator = navigator;
    }

    public void onLoginSuccess() {
         navigator.pop();
    }
}
