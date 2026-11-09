package app.views.fx.pages.home;

import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import app.models.walletmodel.WalletModel;
import app.models.walletmodel.WalletModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.FxUserBoxController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import permissions.Permission;

public class FxUserBoxView extends FxView<FxUserBoxController> implements UserModelListener, WalletModelListener {

    private final UserModel userModel;
    private final WalletModel walletModel;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label coinsLabel;

    public FxUserBoxView(FxUserBoxController controller, UserModel userModel, WalletModel walletModel) {
        this.userModel = userModel;
        this.walletModel = walletModel;
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/home/UserBox.fxml";
    }

    @Override
    protected void onLoaded() {
        subscribe(walletModel.addListener(this));
        subscribe(userModel.addListener(this));

        if (userModel.hasPermission(Permission.USE_COINS)) {
            controller.getBalance().exceptionally(_ -> {
                //Throwable cause = error.getCause() != null ? error.getCause() : error;
                // Still not done, we'll need a notification system!
                return null;
            });
        }
        updateUI(this::update);
    }

    private void update() {

        if (userModel.isLoggedIn()) {
            usernameLabel.setText(userModel.getUsername());

            updateCoins();
        }
    }

    private void updateCoins() {
        if (userModel.hasPermission(Permission.USE_COINS)) {

            coinsLabel.setManaged(true);
            coinsLabel.setVisible(true);

            coinsLabel.setText(String.format("%s",walletModel.getBalance()));
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

    @Override
    public void onBalanceUpdated() {
        updateUI(this::updateCoins);
    }
}
