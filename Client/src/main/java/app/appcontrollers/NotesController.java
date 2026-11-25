package app.appcontrollers;

import app.models.notemodel.NoteModel;
import app.utils.PDFObject;
import app.utils.PDFRenderer;

import java.io.IOException;
import java.nio.file.Path;

/**
 * This controller is NOT testable at the moment because we can't mock the renderer!
 */
public class NotesController {

    private final NoteModel noteModel;
    private PDFRenderer renderer;

    public NotesController(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    /**
     * This function should prepare the renderer and get the PDFObject built from the renderer to prepare the pages
     * in the view.
     * @param path The PDF file path
     * @throws IOException in case of errors opening the PDF file
     */
    public void openPDF(String path) throws IOException {
        renderer = new PDFRenderer(this);
        PDFObject pdfObject = renderer.open(Path.of(path));
        noteModel.pdfOpened(pdfObject);
    }

    /**
     * This function should make the renderer render a range of pages. Rendered pages are then notified to the view
     * to be shown.
     * @param visiblePage the page that the user is watching
     */
    public void renderRange(int visiblePage) {
        renderer.renderRange(visiblePage);
    }

    /**
     * This function should be called from PDFRenderer when a page was rendered
     * @param pageIndex the rendered page index
     * @param image the rendered image
     */
    public void pageRendered(int pageIndex, byte[] image) {
        noteModel.pageRendered(pageIndex, image);
    }

    /**
     * This function should be called from PDFRenderer when a page's image was deleted
     * @param pageIndex the rendered page index
     */
    public void pageDeleted(int pageIndex) {
        noteModel.pageDeleted(pageIndex);
    }

    /**
     * This function should destroy the renderer
     */
    public void closeNote() {
        renderer.destroy();
    }
}
