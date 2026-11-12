package app.views.fx.pages.notification;

import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import app.models.walletmodel.WalletModel;
import app.models.walletmodel.WalletModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.notification.FxNotificationController;
import permissions.Permission;

public class FxNotificationView extends FxView<FxNotificationController> {

    public FxNotificationView(FxNotificationController controller) {
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/notification/Notification.fxml";
    }

    @Override
    protected void onLoaded() {
    }

    @Override
    protected void onClose() {
        // Still not done :D
    }
}
