package app.views.fx.pages.dialog;

import app.utils.Locales;
import app.utils.Utils;
import app.utils.dialogs.*;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.dialog.FxDialogViewController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FxDialogView extends FxView<FxDialogViewController> {

    @FXML
    private Label titleLabel;
    @FXML
    private VBox fieldsContainer;
    @FXML
    private Label errorLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    private Consumer<Map<String, String>> out;
    private final Map<String, Node> inputNodes = new HashMap<>();
    private final Map<String, String> inputs = new HashMap<>();

    public FxDialogView(FxDialogViewController controller) {
        super(controller);
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/dialog/Dialog.fxml";
    }

    @Override
    protected void onLoaded() {

        cancelButton.setOnAction(_ -> controller.cancelButtonPressed(out));
        confirmButton.setOnAction(_ -> {

            for (Map.Entry<String, Node> entry : inputNodes.entrySet()) {

                switch (entry.getValue()) {

                    case TextField tf -> inputs.put(entry.getKey(), tf.getText());

                    case ComboBox<?> cb -> inputs.put(entry.getKey(), cb.getValue() == null ? "" : cb.getValue().toString());

                    case Button _ -> {
                        // No-op: buttons will populate inputs from the functions in controller
                    }

                    default -> throw new IllegalStateException("Unsupported input node: " + entry.getValue());
                }
            }

            controller.confirmButtonPressed(out, inputs);
        });
    }

    public void populate(String title, DialogSpecs specs, Consumer<Map<String, String>> out) {
        this.out = out;
        getRoot();

        titleLabel.setText(title);

        for (DialogField field : specs.getFields()) {

            Node node = switch (field) {
                case DialogTextField dtf -> {
                    TextField textField = new TextField();
                    textField.setPromptText(dtf.getPrompt());
                    yield textField;
                }
                case DialogComboBoxField dcbf -> {
                    ComboBox<?> cb = new ComboBox<>(FXCollections.observableArrayList(dcbf.getOptions()));
                    cb.setMaxWidth(Double.MAX_VALUE);
                    cb.setPromptText(Locales.get("dialog.select"));
                    yield cb;
                }
                case DialogFileChooserField dfcf -> {
                    Button button = new Button(Locales.get("choose_file"));
                    button.setOnAction(_ -> controller.fileChooserButtonPressed(dfcf.getId(), inputs, button.getScene().getWindow()));
                    yield button;
                }
                default -> null;
            };

            if (node == null) continue;

            inputNodes.put(field.getId(), node);
            fieldsContainer.getChildren().add(node);
        }
    }

    @Override
    protected void onClose() {
        // No-op
    }
}
