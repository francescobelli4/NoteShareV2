package app.views.fx.pages;

import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.FxHomeViewController;

public class FxHomeView extends FxView<FxHomeViewController> {

    public FxHomeView(FxHomeViewController controller) {
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/home/HomePage.fxml";
    }

    @Override
    protected void onLoaded() {
        // Still not done :D
    }

    @Override
    protected void onClose() {
        // Still not done :D
    }
}
