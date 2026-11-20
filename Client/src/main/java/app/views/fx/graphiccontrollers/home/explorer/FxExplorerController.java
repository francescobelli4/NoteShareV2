package app.views.fx.graphiccontrollers.home.explorer;

import app.appcontrollers.LibraryController;
import app.entities.ExplorerItemEntity;
import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;
import app.views.GraphicController;

public class FxExplorerController implements GraphicController {

    private final LibraryController libraryController;

    public FxExplorerController(LibraryController libraryController) {
        this.libraryController = libraryController;
    }

    public void onItemDoubleClicked(ExplorerItemEntity itemEntity) {

        switch (itemEntity) {
            case LibraryFolderEntity libFolder -> libraryController.setActiveFolder(libFolder);
            case LibraryNoteEntity _ -> { /* ... */ }
        }
    }
}
