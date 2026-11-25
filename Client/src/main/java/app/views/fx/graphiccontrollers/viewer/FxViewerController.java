package app.views.fx.graphiccontrollers.viewer;

import app.appcontrollers.NotesController;
import app.appcontrollers.NotificationController;
import app.entities.LibraryNoteEntity;
import app.utils.Locales;
import app.views.GraphicController;
import app.views.fx.FxNavigator;

import java.io.IOException;

public class FxViewerController implements GraphicController {
    private final NotesController notesController;
    private final NotificationController notificationController;
    private final FxNavigator navigator;

    public FxViewerController(NotesController notesController, NotificationController notificationController, FxNavigator navigator) {
        this.notesController = notesController;
        this.notificationController = notificationController;
        this.navigator = navigator;
    }

    public void openNote(LibraryNoteEntity noteEntity) {

        try {
            notesController.openPDF(noteEntity.getPath());
        } catch (IOException _) {
            notificationController.showError(Locales.get("error"), Locales.get("could_not_open_note"));
            closeViewer();
        }
    }

    public void closeViewer() {
        navigator.closeOverlay();
    }

    public void onNewPageChanged(int pageIndex) {
        notesController.renderRange(pageIndex);
    }

    public void onViewerClose() {
        notesController.closeNote();
    }
}
