package app.views.fx;

import app.views.GraphicController;
import app.views.View;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;

public abstract class FxView<C extends GraphicController> extends View<C> {

    protected FxView(C controller) {
        super(controller);
    }

    private Parent root;

    protected abstract String getFxmlPath();

    /**
     * This function should lazy-load the root element for this view.
     * @return the fxml object
     */
    public Parent getRoot() {

        if (root == null) {

            URL url = getClass().getResource(getFxmlPath());
            if (url == null) {
                throw new IllegalStateException(String.format("FXML not found: %s", getFxmlPath()));
            }

            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setController(this);

            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            onLoaded();
        }

        return root;
    }

    /**
     * This should be called AFTER the fxml is loaded
     */
    protected abstract void onLoaded();

    /**
     * This function allows to edit the UI only on the JavaFX thread
     * @param runnable the ui update
     */
    protected void updateUI(Runnable runnable) {
        Platform.runLater(runnable);
    }
}
