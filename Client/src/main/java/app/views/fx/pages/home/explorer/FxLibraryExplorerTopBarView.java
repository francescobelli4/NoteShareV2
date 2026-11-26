package app.views.fx.pages.home.explorer;

import app.entities.ExplorerItemEntity;
import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;
import app.models.explorermodel.ExplorerMode;
import app.models.explorermodel.ExplorerModel;
import app.models.explorermodel.ExplorerModelListener;
import app.models.librarymodel.LibraryModel;
import app.models.librarymodel.LibraryModelListener;
import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.explorer.FxLibraryExplorerTopBarController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.List;


public class FxLibraryExplorerTopBarView extends FxView<FxLibraryExplorerTopBarController> implements LibraryModelListener, UserModelListener, ExplorerModelListener {

    @FXML
    private Button backButton;
    @FXML
    private Button newFolderButton;
    @FXML
    private Button newNoteButton;
    @FXML
    private Button copyButton;
    @FXML
    private Button pasteButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label pathLabel;

    private final LibraryModel libraryModel;
    private final UserModel userModel;
    private final ExplorerModel explorerModel;

    public FxLibraryExplorerTopBarView(FxLibraryExplorerTopBarController controller, LibraryModel libraryModel, UserModel userModel, ExplorerModel explorerModel) {
        super(controller);
        this.libraryModel = libraryModel;
        this.userModel = userModel;
        this.explorerModel = explorerModel;
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/home/LibraryExplorerTopBar.fxml";
    }

    @Override
    protected void onLoaded() {
        subscribe(libraryModel.addListener(this));
        subscribe(userModel.addListener(this));
        subscribe(explorerModel.addListener(this));

        controller.loadLibrary();

        newFolderButton.setOnAction(_ -> controller.onNewFolderButtonPressed());
        newNoteButton.setOnAction(_ -> controller.onNewNoteButtonPressed());
        backButton.setOnAction(_ -> controller.onBackButtonClicked());

        copyButton.setOnAction(_ -> controller.onCopyButtonPressed());
        pasteButton.setOnAction(_ -> controller.onPasteButtonPressed());
        deleteButton.setOnAction(_ -> controller.onDeleteButtonPressed());
    }

    private void update(LibraryFolderEntity activeFolder) {
        pathLabel.setText(activeFolder.getPath());
        backButton.setDisable(activeFolder.getParentFolder() == null);
    }

    @Override
    protected void onClose() {
        // Still not done :D
    }

    @Override
    public void activeFolderUpdated(LibraryFolderEntity activeFolder) {
        updateUI(() -> update(activeFolder));
    }

    @Override
    public void clipboardSet(ExplorerItemEntity item) {
        updateUI(() -> pasteButton.setDisable(false));
    }

    @Override
    public void clipboardUnset() {
        updateUI(() -> pasteButton.setDisable(true));
    }

    @Override
    public void folderDeleted(LibraryFolderEntity folderEntity) {
        // No-op
    }

    @Override
    public void noteDeleted(LibraryNoteEntity noteEntity) {
        // No-op
    }

    @Override
    public void onLoggedIn() {
        updateUI(() -> update(libraryModel.getActiveFolder()));
    }

    @Override
    public void onLoggedOut() {
        updateUI(() -> update(libraryModel.getActiveFolder()));
    }

    @Override
    public void itemsUpdated(List<ExplorerItemEntity> newItems) {
        updateUI(() -> updateCopyAndDeleteButtons(false));
    }

    @Override
    public void explorerModeUpdated(ExplorerMode mode) {
        updateUI(() -> updateCopyAndDeleteButtons(false));
    }

    @Override
    public void onItemSelected(ExplorerItemEntity itemEntity) {
        updateUI(() -> updateCopyAndDeleteButtons(true));
    }

    @Override
    public void onItemDeSelected() {
        updateUI(() -> updateCopyAndDeleteButtons(false));
    }

    private void updateCopyAndDeleteButtons(boolean enable) {
        copyButton.setDisable(!enable);
        deleteButton.setDisable(!enable);
    }
}
