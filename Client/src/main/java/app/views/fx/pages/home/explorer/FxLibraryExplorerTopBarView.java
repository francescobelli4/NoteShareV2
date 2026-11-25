package app.views.fx.pages.home.explorer;

import app.entities.LibraryFolderEntity;
import app.models.librarymodel.LibraryModel;
import app.models.librarymodel.LibraryModelListener;
import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.explorer.FxLibraryExplorerTopBarController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;



public class FxLibraryExplorerTopBarView extends FxView<FxLibraryExplorerTopBarController> implements LibraryModelListener, UserModelListener {

    @FXML
    private Button backButton;
    @FXML
    private Button newFolderButton;
    @FXML
    private Button newNoteButton;
    @FXML
    private Label pathLabel;

    private final LibraryModel libraryModel;
    private final UserModel userModel;

    public FxLibraryExplorerTopBarView(FxLibraryExplorerTopBarController controller, LibraryModel libraryModel, UserModel userModel) {
        super(controller);
        this.libraryModel = libraryModel;
        this.userModel = userModel;
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/home/LibraryExplorerTopBar.fxml";
    }

    @Override
    protected void onLoaded() {
        subscribe(libraryModel.addListener(this));
        subscribe(userModel.addListener(this));

        controller.loadLibrary();

        newFolderButton.setOnAction(_ -> controller.onNewFolderButtonPressed());
        newNoteButton.setOnAction(_ -> controller.onNewNoteButtonPressed());
        backButton.setOnAction(_ -> controller.onBackButtonClicked());
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
    public void onLoggedIn() {
        updateUI(() -> update(libraryModel.getActiveFolder()));
    }

    @Override
    public void onLoggedOut() {
        updateUI(() -> update(libraryModel.getActiveFolder()));
    }
}
