package app.views.fx.pages;

import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.FxHomeViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class FxHomeView extends FxView<FxHomeViewController> {

    @FXML
    Button loginButton;

    public FxHomeView(FxHomeViewController controller) {
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/home/HomePage.fxml";
    }

    @Override
    protected void onLoaded() {
        loginButton.setOnAction(event -> controller.onLoginButtonClicked());
    }

    @Override
    protected void onClose() {
        // Still not done :D
    }
}
