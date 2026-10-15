package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static void launchApp() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(new Scene(new StackPane(new Label("Test :D")), 400, 300));
        stage.setTitle("NoteShare");
        stage.show();
    }
}
