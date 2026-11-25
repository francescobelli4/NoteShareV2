package app.models.notemodel;

import app.utils.ListenerHelper;
import app.utils.PDFObject;
import app.utils.Subscription;

/**
 * This model represents a note in the app. Actually, we only need to be able to view the note, so this class should
 * only provide events to notify the view when a page is rendered or deleted from memory.
 */
public class NoteModel {

    private final ListenerHelper<NoteModelListener> listeners = new ListenerHelper<>();

    public Subscription addListener(NoteModelListener listener) {
        return listeners.subscribe(listener);
    }

    public void pdfOpened(PDFObject pdfObject) {
        listeners.triggerEvent(listener -> listener.onPdfOpened(pdfObject));
    }

    public void pageRendered(int index, byte[] image) {
        listeners.triggerEvent(listener -> listener.onPageRendered(index, image));
    }

    public void pageDeleted(int index) {
        listeners.triggerEvent(listener -> listener.onPageDeleted(index));
    }
}
