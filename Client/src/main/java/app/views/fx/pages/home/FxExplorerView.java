package app.views.fx.pages.home;

import app.entities.ExplorerItemEntity;
import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;
import app.models.explorermodel.ExplorerModel;
import app.models.explorermodel.ExplorerModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.FxFolderExplorerController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;

import java.util.List;
import java.util.Objects;


public class FxExplorerView extends FxView<FxFolderExplorerController> implements ExplorerModelListener {

    @FXML
    private Button backButton;
    @FXML
    private Button newFolderButton;
    @FXML
    private Button newNoteButton;
    @FXML
    private Label pathLabel;
    @FXML
    private Label emptyLabel;
    @FXML
    private FlowPane itemsPane;

    private Image folderIcon;
    private Image noteIcon;

    private final ExplorerModel explorerModel;

    public FxExplorerView(FxFolderExplorerController controller, ExplorerModel explorerModel) {
        super(controller);
        this.explorerModel = explorerModel;
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/home/Explorer.fxml";
    }

    @Override
    protected void onLoaded() {
        subscribe(explorerModel.addListener(this));
        controller.loadLibrary();

        folderIcon = new Image(Objects.requireNonNull(getClass().getResource("/fxml/styles/icons/folder.png")).toExternalForm());
        noteIcon   = new Image(Objects.requireNonNull(getClass().getResource("/fxml/styles/icons/file.png")).toExternalForm());

        newFolderButton.setOnAction(_ -> controller.onNewFolderButtonPressed());
        newNoteButton.setOnAction(_ -> controller.onNewNoteButtonPressed());
        backButton.setOnAction(_ -> controller.onBackButtonClicked());
    }

    private void update(List<ExplorerItemEntity> newItems) {

        /* QUESTA ROBA DOVRà STARE IN UNA NUOVA VIEW CHE SAREBBE LIBRARYEXPLORERTOPBAR, CHE DOVRà OSSERVARE LIBRARYMODEL
        pathLabel.setText(activeFolder.getPath());
        backButton.setDisable(activeFolder.getParentFolder() == null);
         */

        itemsPane.getChildren().clear();

        for (ExplorerItemEntity item : newItems) {

            switch (item) {
                case LibraryFolderEntity libFolder -> itemsPane.getChildren().add(new ExplorerItem(libFolder.getName(), folderIcon, () -> controller.onFolderDoubleClicked(libFolder)));
                case LibraryNoteEntity libNote -> itemsPane.getChildren().add(new ExplorerItem(libNote.getName(), noteIcon, () -> controller.onNoteDoubleClicked(libNote)));

                default -> {
                    // Nothing for now
                }
            }
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
}
