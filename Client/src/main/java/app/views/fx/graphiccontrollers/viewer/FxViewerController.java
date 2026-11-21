package app.views.fx.graphiccontrollers.viewer;

import app.appcontrollers.AuthController;
import app.appcontrollers.NotesController;
import app.beans.LoginUsingTokenBean;
import app.entities.LibraryNoteEntity;
import app.utils.TokenManager;
import app.views.GraphicController;
import app.views.fx.FxNavigator;
import app.views.fx.FxViewFactory;

import java.io.IOException;

public class FxViewerController implements GraphicController {
    private final NotesController notesController;

    public FxViewerController(NotesController notesController) {
        this.notesController = notesController;
    }

    public void openNote(LibraryNoteEntity noteEntity) {
        notesController.openPDF(noteEntity.getPath());
    }
}
