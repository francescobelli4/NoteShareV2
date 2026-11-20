package app.models.notificationmodel;

import app.utils.ListenerHelper;
import app.utils.NotificationType;
import app.utils.Subscription;

/**
 * This model represents notifications in the app. When a new notification arrives, it should notify the listeners
 * to create the proper notification view to display that notification.
 */
public class NotificationModel {

    private final ListenerHelper<NotificationModelListener> listeners = new ListenerHelper<>();

    public Subscription addListener(NotificationModelListener l) { return listeners.subscribe(l); }

    public void show(NotificationType notificationType, String title, String description) {
        listeners.triggerEvent(l -> l.onNotification(notificationType, title, description));
    }
}
