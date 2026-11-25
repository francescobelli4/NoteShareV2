package app.models.notemodel;

import app.utils.PDFObject;

public interface NoteModelListener {
    void onPdfOpened(PDFObject pdfObject);
    void onPageRendered(int pageIndex, byte[] image);

    void onPageDeleted(int pageIndex);
}
