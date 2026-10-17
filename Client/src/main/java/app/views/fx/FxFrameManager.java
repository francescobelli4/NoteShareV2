package app.views.fx;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**
 * This class should manage how the main frame gets populated. The frame is made of a pageContainer and an
 * overlayContainer.
 * This class should actually decide the FxView that is visualized by the user, but it should not close it when that
 * page gets hidden: this is done by the Navigator.
 * Overlays, instead, do not appear in Navigator's stack, and they can be closed immediately when they get hidden.
 */
public class FxFrameManager {

    private FxView currentPage, currentOverlay;
    private final StackPane pageContainer = new StackPane();
    private final StackPane overlayContainer = new StackPane();
    private final StackPane frame = new StackPane(pageContainer, overlayContainer);

    public FxFrameManager() {
        overlayContainer.setPickOnBounds(false);
    }

    public void show(FxView view) {
        closeOverlay();

        currentPage = view;
        pageContainer.getChildren().setAll(view.getRoot());
    }

    public void showOverlay(FxView view) {

        closeOverlay();

        currentOverlay = view;
        overlayContainer.getChildren().setAll(view.getRoot());
    }

    public void closeOverlay() {
        if (currentOverlay != null) {
            currentOverlay.close();
            currentOverlay = null;
        }

        overlayContainer.getChildren().clear();
    }

    public Parent getFrame() {
        return frame;
    }
}
