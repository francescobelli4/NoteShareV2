package app.views.fx.graphiccontrollers.home;

import app.appcontrollers.ProfileController;
import app.views.GraphicController;
import java.util.concurrent.CompletableFuture;

public class FxUserBoxController implements GraphicController {

    private final ProfileController profileController;

    public FxUserBoxController(ProfileController profileController) {
        this.profileController = profileController;
    }

    public CompletableFuture<Void> getBalance() {
        return profileController.getBalance();
    }
}
