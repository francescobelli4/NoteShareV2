package app.views.fx;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

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

        if (currentPage != null) currentPage.close();
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
