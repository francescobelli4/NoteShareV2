package app.views.fx.pages.home;

import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.FxHomeViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class FxHomeView extends FxView<FxHomeViewController> implements UserModelListener {

    private final UserModel userModel;
    @FXML
    Button loginButton;

    public FxHomeView(FxHomeViewController controller, UserModel userModel) {
        this.userModel = userModel;
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/home/HomePage.fxml";
    }

    @Override
    protected void onLoaded() {
        subscribe(userModel.addListener(this));
        loginButton.setOnAction(_ -> controller.onLoginButtonClicked());
    }

    @Override
    protected void onClose() {
        // Still not done :D
    }

    @Override
    public void onLoggedIn() {
        // Still not done :D
    }

    @Override
    public void onLoggedOut() {
        // Still not done :D
    }
}
