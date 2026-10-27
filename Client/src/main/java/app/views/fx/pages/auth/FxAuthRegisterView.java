package app.views.fx.pages.auth;

import app.exceptions.AuthException;
import app.models.usermodel.UserModel;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.auth.FxAuthLoginViewController;
import app.views.fx.graphiccontrollers.auth.FxAuthRegisterViewController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import permissions.Role;

import java.util.Objects;

public class FxAuthRegisterView extends FxView<FxAuthRegisterViewController> {

    private final UserModel userModel;

    private PauseTransition errorTimer;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Label errorLabel;
    @FXML
    private Button registerButton;

    public FxAuthRegisterView(FxAuthRegisterViewController controller, UserModel userModel) {
        this.userModel = userModel;
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/auth/RegisterCard.fxml";
    }

    @Override
    protected void onLoaded() {
        registerButton.setOnAction(_ -> {

            try {
                controller.registerButtonClicked(usernameField.getText(), passwordField.getText(), confirmPasswordField.getText(), roleComboBox.getValue());
            } catch (AuthException e) {
                updateUI(() -> showError(e.getMessage()));
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
