package app.views.fx;

import app.views.View;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;

public abstract class FxView extends View {

    private Parent root;

    protected String getFxmlPath() {
        return null;
    }

    /**
     * This function should lazy-load the root element for this view.
     * @return the fxml object
     */
    public Parent getRoot() {

        if (root == null) {

            URL url = getClass().getResource(getFxmlPath());
            FXMLLoader fxmlLoader = new FXMLLoader(url);

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
    protected void onLoaded() {}

    /**
     * This function allows to edit the UI only on the JavaFX thread
     * @param runnable the ui update
     */
    protected void updateUI(Runnable runnable) {
        Platform.runLater(runnable);
    }

    @Override
    protected void onClose() {
        super.onClose();
    }
}
