package app.views.fx.pages.home.explorer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class FxExplorerItem extends VBox {

    public FxExplorerItem(String name, Image icon, Runnable onClick, Runnable onDoubleClick) {
        super(5);

        getStyleClass().add("explorer-item");

        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(56);
        imageView.setFitHeight(56);
        imageView.setPreserveRatio(true);

        Label label = new Label(name);
        label.setWrapText(true);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);

        getChildren().addAll(imageView, label);

        setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) onDoubleClick.run();
            if (e.getClickCount() == 1) onClick.run();
        });
    }

    public void setSelected(boolean selected) {
        getStyleClass().remove("explorer-item-selected");
        if (selected) getStyleClass().add("explorer-item-selected");
    }
}
