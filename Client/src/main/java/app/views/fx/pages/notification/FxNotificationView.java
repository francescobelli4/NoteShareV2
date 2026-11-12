package app.views.fx.pages.notification;

import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import app.models.walletmodel.WalletModel;
import app.models.walletmodel.WalletModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.notification.FxNotificationController;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class FxNotificationView extends FxView<FxNotificationController> {

    @FXML
    private ImageView image;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;

    public FxNotificationView(FxNotificationController controller) {
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/notification/Notification.fxml";
    }

    @Override
    protected void onLoaded() {
        titleLabel.setText("TITLE");
        descriptionLabel.setText("Description description description description description description description description description");

        root.heightProperty().addListener((_, _, newH) -> {
            if (newH.doubleValue() > 0) {
                animate();
            }
        });
    }

    @Override
    protected void onClose() {
        // Still not done :D
    }

    private void animate() {
        StackPane parent = (StackPane) ViewNavigator.getActiveView().getRoot();
        StackPane.setAlignment(root, Pos.TOP_CENTER);

        root.setTranslateY(-root.getHeight());

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), root);
        slideIn.setFromY(-root.getHeight());
        slideIn.setToY(ViewNavigator.getStage().getHeight()/25);
        slideIn.play();

        slideIn.setOnFinished(_ -> {

            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(_ -> {

                TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), root);
                slideOut.setFromY(ViewNavigator.getStage().getHeight()/25);
                slideOut.setToY(-root.getHeight());

                slideOut.setOnFinished(_ -> parent.getChildren().remove(root));
                slideOut.play();
            });
            delay.play();
        });
    }
}
