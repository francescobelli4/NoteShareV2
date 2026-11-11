package app.views.fx.pages.home;

import app.entities.FolderEntity;
import app.models.librarymodel.LibraryModel;
import app.models.librarymodel.LibraryModelListener;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.FxFolderExplorerController;

public class FxFolderExplorerView extends FxView<FxFolderExplorerController> implements LibraryModelListener {

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
    }


    @Override
    protected void onClose() {
        // Still not done :D
    }

    @Override
    public void activeFolderUpdated(FolderEntity folderEntity) {

    }
}
