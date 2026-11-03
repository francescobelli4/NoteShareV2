package app.views.fx.pages.home;

import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.FxHomeViewController;
import app.views.fx.graphiccontrollers.home.FxUserBoxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FxUserBoxView extends FxView<FxUserBoxController> implements UserModelListener {

    private final UserModel userModel;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label coinsLabel;

    public FxUserBoxView(FxUserBoxController controller, UserModel userModel) {
        this.userModel = userModel;
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/home/UserBox.fxml";
    }

    @Override
    protected void onLoaded() {
        subscribe(userModel.addListener(this));
        updateUI(this::update);
    }

    private void update() {

        if (userModel.isLoggedIn()) {
            usernameLabel.setText(userModel.getUsername());
        }
    }

    @Override
    protected void onClose() {
        // Still not done :D
    }

    @Override
    public void onLoggedIn() {
       updateUI(this::update);
    }

    @Override
    public void onLoggedOut() {
        // Still not done :D
    }
}
