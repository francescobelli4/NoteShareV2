package app;

import app.views.fx.FxNavigator;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Launcher extends Application {

    private static final double MIN_WIDTH = 900;
    private static final double MIN_HEIGHT = 600;

    public static void launchApp() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        FxNavigator navigator = new FxNavigator();

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        double minWidth = Math.min(MIN_WIDTH, screen.getWidth());
        double minHeight = Math.min(MIN_HEIGHT, screen.getHeight());

        double width = Math.max(minWidth, screen.getWidth() * 0.8);
        double height = Math.max(minHeight, screen.getHeight() * 0.8);

        Scene scene = new Scene(navigator.getFrame(), width, height);

        stage.setScene(scene);
        stage.setTitle("NoteShare");
        stage.show();
    }
}
