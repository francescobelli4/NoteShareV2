package app.appcontrollers;

import app.models.notemodel.NoteModel;
import app.utils.PDFRenderer;

import java.nio.file.Path;

public class NotesController {

    private final NoteModel noteModel;
    private final PDFRenderer renderer = new PDFRenderer();

    public NotesController(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    public void openPDF(String path) {
        noteModel.pdfOpened(renderer.open(Path.of(path)));
    }
}
