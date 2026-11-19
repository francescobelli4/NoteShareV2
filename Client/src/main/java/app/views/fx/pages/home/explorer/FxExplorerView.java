package app.views.fx.pages.home.explorer;

import app.entities.ExplorerItemEntity;
import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;
import app.models.explorermodel.ExplorerMode;
import app.models.explorermodel.ExplorerModel;
import app.models.explorermodel.ExplorerModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.explorer.FxExplorerController;
import app.views.fx.pages.home.ExplorerItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;


public class FxExplorerView extends FxView<FxExplorerController> implements ExplorerModelListener {

    private final Supplier<FxLibraryExplorerTopBarView> libraryExplorerTopBarViewSupplier;
    private FxView<?> topBarView;

    @FXML
    private StackPane topBarContainer;
    @FXML
    private Label emptyLabel;
    @FXML
    private FlowPane itemsPane;

    private Image folderIcon;
    private Image noteIcon;

    private final ExplorerModel explorerModel;

    public FxExplorerView(FxExplorerController controller, ExplorerModel explorerModel, Supplier<FxLibraryExplorerTopBarView> libraryExplorerTopBarViewSupplier) {
        super(controller);
        this.explorerModel = explorerModel;
        this.libraryExplorerTopBarViewSupplier = libraryExplorerTopBarViewSupplier;
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/home/Explorer.fxml";
    }

    @Override
    protected void onLoaded() {
        subscribe(explorerModel.addListener(this));

        folderIcon = new Image(Objects.requireNonNull(getClass().getResource("/fxml/styles/icons/folder.png")).toExternalForm());
        noteIcon   = new Image(Objects.requireNonNull(getClass().getResource("/fxml/styles/icons/file.png")).toExternalForm());

        explorerModel.setMode(ExplorerMode.LIBRARY);
    }

    private void update(List<ExplorerItemEntity> newItems) {

        itemsPane.getChildren().clear();

        for (ExplorerItemEntity item : newItems) {

            Image icon = switch (item) {
                case LibraryFolderEntity _ -> folderIcon;
                case LibraryNoteEntity _ -> noteIcon;
            };

            itemsPane.getChildren().add(new ExplorerItem(item.getName(), icon, () -> controller.onItemDoubleClicked(item)));
        }

        boolean empty = itemsPane.getChildren().isEmpty();
        emptyLabel.setVisible(empty);
        emptyLabel.setManaged(empty);
    }


    @Override
    protected void onClose() {
        // Still not done :D
    }


    @Override
    public void itemsUpdated(List<ExplorerItemEntity> newItems) {
        updateUI(() -> update(newItems));
    }

    @Override
    public void explorerModeUpdated(ExplorerMode mode) {
        updateUI(() -> updateTopBar(mode));
    }

    private void updateTopBar(ExplorerMode mode) {

        switch (mode) {
            case LIBRARY -> setTopBar(libraryExplorerTopBarViewSupplier.get());
            default -> clearTopBar();
        }
    }

    private void setTopBar(FxView<?> view) {

        clearTopBar();

        topBarView = view;
        addChild(view);
        topBarContainer.getChildren().setAll(view.getRoot());
    }

    private void clearTopBar() {

        if (topBarView != null) {
            removeChild(topBarView);
            topBarView = null;
        }

        topBarContainer.getChildren().clear();
    }
}
