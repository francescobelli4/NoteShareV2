package app.views.fx.graphiccontrollers.viewer;

import app.appcontrollers.NotesController;
import app.appcontrollers.NotificationController;
import app.entities.LibraryNoteEntity;
import app.utils.Locales;
import app.views.GraphicController;

import java.io.IOException;

public class FxViewerController implements GraphicController {
    private final NotesController notesController;
    private final NotificationController notificationController;

    public FxViewerController(NotesController notesController, NotificationController notificationController) {
        this.notesController = notesController;
        this.notificationController = notificationController;
    }

    public void openNote(LibraryNoteEntity noteEntity) {

        try {
            notesController.openPDF(noteEntity.getPath());
        } catch (IOException _) {
            notificationController.showError(Locales.get("error"), Locales.get("could_not_open_note"));
        }
    }

    public void onNewPageChanged(int pageIndex) {
        notesController.renderRange(pageIndex);
    }

    public void onViewerClose() {
        notesController.closeNote();
    }
}
