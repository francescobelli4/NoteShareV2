package app.views.fx.graphiccontrollers.home.explorer;

import app.appcontrollers.LibraryController;
import app.entities.ExplorerItemEntity;
import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;
import app.views.GraphicController;
import app.views.fx.FxNavigator;
import app.views.fx.FxViewFactory;
import app.views.fx.pages.viewer.FxViewerView;

public class FxExplorerController implements GraphicController {

    private final LibraryController libraryController;
    private final FxNavigator navigator;
    private final FxViewFactory factory;

    public FxExplorerController(LibraryController libraryController, FxNavigator navigator, FxViewFactory factory) {
        this.libraryController = libraryController;
        this.navigator = navigator;
        this.factory = factory;
    }

    public void onItemDoubleClicked(ExplorerItemEntity itemEntity) {

        switch (itemEntity) {

            case LibraryFolderEntity libFolder -> libraryController.setActiveFolder(libFolder);

            case LibraryNoteEntity libNote -> {
                FxViewerView fxViewerView = factory.createFxViewerView();
                navigator.showOverlay(factory.createFxViewerView());
                fxViewerView.setNote(libNote);
            }
        }
    }
}
