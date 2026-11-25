package app.models.notemodel;

import app.utils.PDFObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NoteModelTest {

    private static final NoteModel noteModel = new NoteModel();

    private static NoteModelListener noteModelListener;

    @BeforeAll
    static void beforeAll() {
        noteModelListener = mock(NoteModelListener.class);
        noteModel.addListener(noteModelListener);
    }

    @Test
    void pdfOpened() {
        PDFObject pdfObject = mock(PDFObject.class);
        noteModel.pdfOpened(pdfObject);
        verify(noteModelListener).onPdfOpened(any(PDFObject.class));
    }

    @Test
    void pageRendered() {
        byte[] bytes = new byte[1];
        noteModel.pageRendered(1, bytes);
        verify(noteModelListener).onPageRendered(1, bytes);
    }

    @Test
    void pageDeleted() {
        noteModel.pageDeleted(1);
        verify(noteModelListener).onPageDeleted(1);
    }
}