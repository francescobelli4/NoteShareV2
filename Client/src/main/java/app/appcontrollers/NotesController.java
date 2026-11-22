package app.appcontrollers;

import app.models.notemodel.NoteModel;
import app.utils.PDFRenderer;

import java.nio.file.Path;

public class NotesController {

    private final NoteModel noteModel;
    private PDFRenderer renderer;

    public NotesController(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    public void openPDF(String path) {
        renderer = new PDFRenderer(this);
        noteModel.pdfOpened(renderer.open(Path.of(path)));
    }

    public void renderRange(int visiblePage) {
        renderer.renderRange(visiblePage);
    }

    public void pageRendered(int pageIndex, byte[] image) {
        noteModel.pageRendered(pageIndex, image);
    }

    public void pageDeleted(int pageIndex) {
        noteModel.pageDeleted(pageIndex);
    }

    public void closeNote() {
        renderer.destroy();
    }
}
