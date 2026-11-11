package app.views.fx.graphiccontrollers.home;

import app.appcontrollers.LibraryController;
import app.views.GraphicController;

public class FxFolderExplorerController implements GraphicController {

    private final LibraryController libraryController;

    public FxFolderExplorerController(LibraryController libraryController) {
        this.libraryController = libraryController;
    }

    public void loadLibrary() {
        libraryController.loadLibrary();
    }
}
