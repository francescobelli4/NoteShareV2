package app.views.fx.graphiccontrollers.home;

import app.appcontrollers.NotificationController;
import app.appcontrollers.ProfileController;
import app.utils.Locales;
import app.views.GraphicController;
import java.util.concurrent.CompletableFuture;

public class FxUserBoxController implements GraphicController {

    private final ProfileController profileController;
    private final NotificationController notificationController;

    public FxUserBoxController(ProfileController profileController, NotificationController notificationController) {
        this.profileController = profileController;
        this.notificationController = notificationController;
    }

    public CompletableFuture<Void> getBalance() {
        return profileController.getBalance();
    }

    public void notifyBalanceError() {
        notificationController.showError(Locales.get("error"), Locales.get("could_not_get_balance"));
    }
}
