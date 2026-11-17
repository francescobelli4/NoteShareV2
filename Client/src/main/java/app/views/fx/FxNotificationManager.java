package app.views.fx;

import app.models.notificationmodel.NotificationModel;
import app.models.notificationmodel.NotificationModelListener;
import app.utils.NotificationType;
import app.views.fx.pages.notification.FxNotificationView;
import javafx.application.Platform;
import javafx.scene.Node;

public class FxNotificationManager implements NotificationModelListener {

    public static final String ICONS_PATH = "/fxml/styles/icons/";

    private final FxFrameManager fxFrameManager;
    private final FxViewFactory fxViewFactory;

    public FxNotificationManager(NotificationModel notificationModel, FxFrameManager fxFrameManager, FxViewFactory fxViewFactory) {
        this.fxFrameManager = fxFrameManager;
        this.fxViewFactory = fxViewFactory;
        notificationModel.addListener(this);
    }

    @Override
    public void onNotification(NotificationType notificationType, String title, String description) {
        runOnFx(() -> {
            FxNotificationView notificationView = fxViewFactory.createFxNotificationView();
            Node root = notificationView.getRoot();

            notificationView.setContent(notificationType, title, description);
            fxFrameManager.showNotification(root);
            notificationView.playAnim(fxFrameManager::removeNotification);
        });
    }

    private void runOnFx(Runnable r) {
        if (Platform.isFxApplicationThread()) r.run();
        else Platform.runLater(r);
    }
}
