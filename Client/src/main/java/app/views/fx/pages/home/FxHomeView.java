package app.views.fx.pages.home;

import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.FxHomeViewController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.function.Supplier;

public class FxHomeView extends FxView<FxHomeViewController> implements UserModelListener {

    private final UserModel userModel;

    private final Supplier<FxUserBoxView> userBoxViewSupplier;

    @FXML
    private Button loginButton;
    @FXML
    private VBox guestBox;
    @FXML
    private StackPane userBoxContainer;

    public FxHomeView(FxHomeViewController controller, UserModel userModel, Supplier<FxUserBoxView> userBoxViewSupplier) {
        this.userModel = userModel;
        this.userBoxViewSupplier = userBoxViewSupplier;
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
        controller.tryLoginUsingToken();

        updateUI(this::update);
    }

    @Override
    protected void onClose() {
        // Still not done :D
    }

    private void update() {
        setShown(guestBox, !userModel.isLoggedIn());
        setShown(userBoxContainer, userModel.isLoggedIn());
    }

    private void setShown(Node node, boolean show) {
        node.setVisible(show);
        node.setManaged(show);
    }

    @Override
    public void onLoggedIn() {

        updateUI(() -> {
            FxUserBoxView userBoxView = userBoxViewSupplier.get();
            addChild(userBoxView);
            userBoxContainer.getChildren().setAll(userBoxView.getRoot());
            update();
        });
    }

    @Override
    public void onLoggedOut() {
        // Still not done :D
    }
}
