package app.models.notemodel;

import app.utils.PDFObject;

public interface NoteModelListener {
    void onPdfOpened(PDFObject pdfObject);
}
