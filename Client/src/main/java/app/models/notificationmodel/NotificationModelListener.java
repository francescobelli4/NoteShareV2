package app.models.notificationmodel;

import app.utils.NotificationType;

public interface NotificationModelListener {

    void onNotification(NotificationType notificationType, String title, String description);
}
