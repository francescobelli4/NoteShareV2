package app.models.notificationmodel;

import app.models.explorermodel.ExplorerModel;
import app.models.explorermodel.ExplorerModelListener;
import app.utils.NotificationType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationModelTest {

    private static final NotificationModel notificationModel = new NotificationModel();
    private static NotificationModelListener notificationModelListener;

    @BeforeAll
    static void beforeAll() {
        notificationModelListener = mock(NotificationModelListener.class);
        notificationModel.addListener(notificationModelListener);
    }

    @Test
    void show() {
        notificationModel.show(NotificationType.SUCCESS, "title", "descritpion");
        verify(notificationModelListener, times(1)).onNotification(NotificationType.SUCCESS, "title", "descritpion");
    }
}