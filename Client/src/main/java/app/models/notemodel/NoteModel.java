package app.models.notemodel;

import app.utils.ListenerHelper;
import app.utils.PDFObject;
import app.utils.Subscription;

public class NoteModel {

    private final ListenerHelper<NoteModelListener> listeners = new ListenerHelper<>();

    public Subscription addListener(NoteModelListener listener) {
        return listeners.subscribe(listener);
    }

    public void pdfOpened(PDFObject pdfObject) {
        listeners.triggerEvent(listener -> listener.onPdfOpened(pdfObject));
    }
}
