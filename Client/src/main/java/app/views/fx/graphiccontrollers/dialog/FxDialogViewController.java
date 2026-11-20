package app.views.fx.graphiccontrollers.dialog;

import app.utils.Utils;
import app.views.GraphicController;
import app.views.fx.FxNavigator;
import javafx.stage.Window;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;

public class FxDialogViewController implements GraphicController {

    private final FxNavigator navigator;

    public FxDialogViewController(FxNavigator navigator) {
        this.navigator = navigator;
    }

    public void cancelButtonPressed(Consumer<Map<String, String>> out) {
        navigator.closeOverlay();
        out.accept(null);
    }

    public void confirmButtonPressed(Consumer<Map<String, String>> out, Map<String, String> inputs) {
        navigator.closeOverlay();
        out.accept(inputs);
    }

    public void fileChooserButtonPressed(String itemId, Map<String, String> inputs, Window window) {
        File chosenFile = Utils.openFileChooser(window);

        if (chosenFile == null) return;

        inputs.put(itemId, chosenFile.getAbsolutePath());
    }
}
