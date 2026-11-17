package app.views.fx.pages.dialog;

import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.dialog.FxDialogViewController;

public class FxDialogView extends FxView<FxDialogViewController> {

    public FxDialogView(FxDialogViewController controller) {
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

    }
}
