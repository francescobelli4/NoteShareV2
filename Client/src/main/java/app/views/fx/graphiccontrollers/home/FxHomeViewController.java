package app.views.fx.graphiccontrollers.home;

import app.views.GraphicController;
import app.views.fx.FxNavigator;
import app.views.fx.FxViewFactory;

public class FxHomeViewController implements GraphicController {

    private final FxNavigator navigator;
    private final FxViewFactory factory;

    public FxHomeViewController(FxNavigator navigator, FxViewFactory factory) {
        this.navigator = navigator;
        this.factory = factory;
    }

    public void onLoginButtonClicked() {
        navigator.push(factory.createFxAuthView());
    }
}
