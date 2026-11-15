package app.appcontrollers;

import app.models.notificationmodel.NotificationModel;
import app.utils.NotificationType;

public class NotificationController {

    private final NotificationModel notificationModel;

    public NotificationController(NotificationModel notificationModel) {
        this.notificationModel = notificationModel;
    }

    public void showError(String title, String description) {
        notificationModel.show(NotificationType.ERROR, title, description);
    }
}
