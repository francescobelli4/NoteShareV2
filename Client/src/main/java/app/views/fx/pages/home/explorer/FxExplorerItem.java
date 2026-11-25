package app.views.fx.pages.home.explorer;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class FxExplorerItem extends VBox {

    public FxExplorerItem(String name, Image icon, Runnable onDoubleClick) {
        super(5);

        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(56);
        imageView.setFitHeight(56);
        imageView.setPreserveRatio(true);

        Label label = new Label(name);
        label.setWrapText(true);
        label.setMaxWidth(100);

        getChildren().addAll(imageView, label);

        setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) onDoubleClick.run();
        });
    }
}
