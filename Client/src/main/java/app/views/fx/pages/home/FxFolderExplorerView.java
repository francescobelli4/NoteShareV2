package app.views.fx.pages.home;

import app.entities.FolderEntity;
import app.entities.NoteEntity;
import app.models.librarymodel.LibraryModel;
import app.models.librarymodel.LibraryModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.FxFolderExplorerController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;

import java.util.Objects;


public class FxFolderExplorerView extends FxView<FxFolderExplorerController> implements LibraryModelListener {

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

    private final LibraryModel libraryModel;

    public FxFolderExplorerView(FxFolderExplorerController controller, LibraryModel libraryModel) {
        super(controller);
        this.libraryModel = libraryModel;
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/home/FolderExplorer.fxml";
    }

    @Override
    protected void onLoaded() {
        subscribe(libraryModel.addListener(this));
        controller.loadLibrary();

        folderIcon = new Image(Objects.requireNonNull(getClass().getResource("/fxml/styles/icons/folder.png")).toExternalForm());
        noteIcon   = new Image(Objects.requireNonNull(getClass().getResource("/fxml/styles/icons/file.png")).toExternalForm());

        newFolderButton.setOnAction(_ -> controller.onNewFolderButtonPressed());
        newNoteButton.setOnAction(_ -> controller.onNewNoteButtonPressed());
        backButton.setOnAction(_ -> controller.onBackButtonClicked());
    }

    private void update() {

        FolderEntity activeFolder = libraryModel.getActiveFolder();

        pathLabel.setText(activeFolder.getPath());
        backButton.setDisable(activeFolder.getParentFolder() == null);

        itemsPane.getChildren().clear();

        for (FolderEntity folder : activeFolder.getSubFolders()) {
            itemsPane.getChildren().add(new ExplorerItem(folder.getName(), folderIcon, () -> controller.onFolderDoubleClicked(folder)));
        }

        for (NoteEntity note : activeFolder.getNotes()) {
            itemsPane.getChildren().add(new ExplorerItem(note.getName(), noteIcon, () -> controller.onNoteDoubleClicked(note)));
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
    public void activeFolderUpdated(FolderEntity folderEntity) {
        updateUI(this::update);
    }
}
