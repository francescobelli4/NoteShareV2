package app.views.fx.pages.home.explorer;

import app.entities.ExplorerItemEntity;
import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;
import app.models.explorermodel.ExplorerMode;
import app.models.explorermodel.ExplorerModel;
import app.models.explorermodel.ExplorerModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.explorer.FxExplorerController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.util.*;
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
    private final Map<ExplorerItemEntity, FxExplorerItem> items = new HashMap<>();

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
        items.clear();

        for (ExplorerItemEntity item : newItems) {

            Image icon = switch (item) {
                case LibraryFolderEntity _ -> folderIcon;
                case LibraryNoteEntity _ -> noteIcon;
            };

            FxExplorerItem fxItem = new FxExplorerItem(item.getName(), icon,
                    () -> controller.onItemClicked(item),
                    () -> controller.onItemDoubleClicked(item));

            items.put(item, fxItem);
            itemsPane.getChildren().add(fxItem);
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
        updateUI(() -> {
            updateTopBar(mode);
            update(new ArrayList<>());

            switch (mode) {
                case LIBRARY -> controller.onLibraryModeSet();
                case STORE -> controller.onExplorerModeSet();
                default -> {
                    // No-op
                }
            }
        });
    }

    @Override
    public void onItemSelected(ExplorerItemEntity itemEntity) {
        items.values().forEach(fxItem -> fxItem.setSelected(false));
        items.get(itemEntity).setSelected(true);
    }

    @Override
    public void onItemDeSelected() {
        items.values().forEach(fxItem -> fxItem.setSelected(false));
    }

    private void updateTopBar(ExplorerMode mode) {

        if (Objects.requireNonNull(mode) == ExplorerMode.LIBRARY) {
            setTopBar(libraryExplorerTopBarViewSupplier.get());
        } else {
            clearTopBar();
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
