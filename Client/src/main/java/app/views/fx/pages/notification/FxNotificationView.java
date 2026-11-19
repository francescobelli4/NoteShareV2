package app.views.fx.pages.notification;

import app.utils.NotificationType;
import app.views.fx.FxNotificationManager;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.notification.FxNotificationController;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.util.Duration;

import java.util.Objects;

/**
 * <a href="https://dev.java/learn/javafx/javafx-animations/">info animazioni...</a>
 */

public class FxNotificationView extends FxView<FxNotificationController> {

    @FXML
    private ImageView image;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;

    // SequentialTransition can execute a "list" of animations.
    private SequentialTransition fullAnim;

    public FxNotificationView(FxNotificationController controller) {
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/notification/Notification.fxml";
    }

    public void setContent(NotificationType notificationType, String title, String description) {

        String icon = FxNotificationManager.ICONS_PATH + switch (notificationType) {
            case INFO ->  "app_icon.png";
            case SUCCESS -> "success.png";
            case ERROR -> "error.png";
        };

        image.setImage(new Image(Objects.requireNonNull(getClass().getResource(icon)).toExternalForm()));
        titleLabel.setText(title);
        descriptionLabel.setText(description);
    }

    public void playAnim(Runnable onFinished) {

        Region root = (Region) getRoot();
        double prefHeight = root.prefHeight(root.getPrefWidth());

        double startY = -prefHeight;
        root.setTranslateY(startY);
        root.setOpacity(0);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), root);
        slideIn.setToY(prefHeight / 10);
        slideIn.setInterpolator(Interpolator.EASE_OUT);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
        fadeIn.setToValue(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), root);
        slideOut.setToY(startY);
        slideOut.setInterpolator(Interpolator.EASE_IN);

        fullAnim = new SequentialTransition(new ParallelTransition(slideIn, fadeIn), pause, slideOut);
        fullAnim.setOnFinished(_ -> onFinished.run());

        fullAnim.play();
    }

    @Override
    protected void onLoaded() {
        // No-op
    }

    @Override
    protected void onClose() {
        if (fullAnim != null) fullAnim.stop();
    }
}
