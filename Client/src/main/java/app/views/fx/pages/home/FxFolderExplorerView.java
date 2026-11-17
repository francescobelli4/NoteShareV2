package app.views.fx.pages.home;

import app.entities.FolderEntity;
import app.models.librarymodel.LibraryModel;
import app.models.librarymodel.LibraryModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.FxFolderExplorerController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


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

        newFolderButton.setOnAction(_ -> {
            // No possibility to choose the name for now... :D
            controller.onNewFolderButtonPressed();
        });
    }

    private void updatePathLabel() {
        pathLabel.setText(libraryModel.getActiveFolder().getPath());
    }


    @Override
    protected void onClose() {
        // Still not done :D
    }

    @Override
    public void activeFolderUpdated(FolderEntity folderEntity) {
        updateUI(this::updatePathLabel);
    }
}
