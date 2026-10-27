package app.views.fx.pages.auth;

import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.auth.FxAuthViewController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;

import java.util.function.Supplier;

public class FxAuthView extends FxView<FxAuthViewController> implements UserModelListener {

    private final UserModel userModel;

    private final Supplier<FxAuthLoginView> loginViewSupplier;
    private final Supplier<FxAuthRegisterView> registerViewSupplier;

    private FxAuthLoginView loginView;
    private FxAuthRegisterView registerView;

    @FXML
    private ToggleGroup authModeGroup;
    @FXML
    private ToggleButton registerToggle;
    @FXML
    private StackPane cardContainer;

    // Using suppliers to lazy-load the two cards :D
    public FxAuthView(FxAuthViewController controller, UserModel userModel, Supplier<FxAuthLoginView> loginViewSupplier, Supplier<FxAuthRegisterView> registerViewSupplier) {
        this.userModel = userModel;
        this.loginViewSupplier = loginViewSupplier;
        this.registerViewSupplier = registerViewSupplier;
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/auth/AuthPage.fxml";
    }

    @Override
    protected void onLoaded() {
        subscribe(userModel.addListener(this));

        authModeGroup.selectedToggleProperty().addListener((_, oldToggle, selected) -> {
            if (selected == null) {
                oldToggle.setSelected(true);
                return;
            }

            // Creating the Login and Register card is mainly UI stuff, so I think the best option is to
            // add that views from this class
            displayCard(selected == registerToggle);
        });

        displayCard(false);
    }

    private void displayCard(boolean isRegister) {

        if (isRegister) {
            if (registerView == null) {
                registerView = registerViewSupplier.get();
                addChild(registerView);
            }

            cardContainer.getChildren().setAll(registerView.getRoot());
        } else {
            if (loginView == null) {
                loginView = loginViewSupplier.get();
                addChild(loginView);
            }

            cardContainer.getChildren().setAll(loginView.getRoot());
        }
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
