package app.appcontrollers;

import app.models.notificationmodel.NotificationModel;
import app.utils.NotificationType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NotificationControllerTest {

    private static NotificationController notificationController;
    private static NotificationModel notificationModel;

    @BeforeAll
    static void beforeAll() {
        notificationModel = mock(NotificationModel.class);
        notificationController = new NotificationController(notificationModel);
    }

    @Test
    void showError() {
        notificationController.showError("title", "desc");
        verify(notificationModel).show(eq(NotificationType.ERROR), anyString(), anyString());
    }

    @Test
    void showInfo() {
        notificationController.showInfo("title", "desc");
        verify(notificationModel).show(eq(NotificationType.INFO), anyString(), anyString());
    }

    @Test
    void showSuccess() {
        notificationController.showSuccess("title", "desc");
        verify(notificationModel).show(eq(NotificationType.SUCCESS), anyString(), anyString());
    }
}