package app.views.fx.graphiccontrollers.dialog;

import app.views.GraphicController;
import app.views.fx.FxNavigator;

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
}
