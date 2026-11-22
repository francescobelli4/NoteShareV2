package app.views.fx.graphiccontrollers.viewer;

import app.appcontrollers.NotesController;
import app.entities.LibraryNoteEntity;
import app.views.GraphicController;

public class FxViewerController implements GraphicController {
    private final NotesController notesController;

    public FxViewerController(NotesController notesController) {
        this.notesController = notesController;
    }

    public void openNote(LibraryNoteEntity noteEntity) {
        notesController.openPDF(noteEntity.getPath());
    }

    public void onNewPageChanged(int pageIndex) {

    }
}
