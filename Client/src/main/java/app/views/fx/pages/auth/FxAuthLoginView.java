package app.views.fx.pages.auth;

import app.exceptions.AuthException;
import app.utils.Locales;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.auth.FxAuthLoginViewController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

public class FxAuthLoginView extends FxView<FxAuthLoginViewController> {

    private PauseTransition errorTimer;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;

    public FxAuthLoginView(FxAuthLoginViewController controller) {
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/auth/LoginCard.fxml";
    }

    @Override
    protected void onLoaded() {
        loginButton.setOnAction(_ -> {

            try {
                controller.onLoginButtonClicked(usernameField.getText(), passwordField.getText()).exceptionally(error -> {
                    // For example, if no user with that username was found or password is wrong
                    Throwable cause = error.getCause() != null ? error.getCause() : error;

                    if (cause instanceof AuthException authException) {
                        updateUI(() -> showError(Locales.getErrorLabel(authException.getAuthError())));
                    } else {
                        updateUI(() -> showError(cause.getMessage()));
                    }

                    return null;
                });
            } catch (AuthException authException) {
                showError(Locales.getErrorLabel(authException.getAuthError()));
            }
        });
    }

    private void showError(String error) {
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
        errorLabel.setText(error);

        if (errorTimer != null) {
            errorTimer.stop();
        }

        errorTimer = new PauseTransition(Duration.seconds(3));
        errorTimer.setOnFinished(_ -> {
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
            errorLabel.setText("");
        });

        errorTimer.play();
    }

    @Override
    protected void onClose() {
        if (errorTimer != null) {
            errorTimer.stop();
        }
    }
}
