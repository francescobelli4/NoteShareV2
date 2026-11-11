package app.models.librarymodel;

import app.entities.FolderEntity;
import app.utils.ListenerHelper;
import app.utils.Subscription;

public class LibraryModel {

    private final ListenerHelper<LibraryModelListener> listeners = new ListenerHelper<>();

    private FolderEntity rootFolder;
    private FolderEntity activeFolder;

    public Subscription addListener(LibraryModelListener listener) {
        return listeners.subscribe(listener);
    }

    public void setRootFolder(FolderEntity folderEntity) {
        rootFolder = folderEntity;
        updateActiveFolder(rootFolder);
    }

    public void updateActiveFolder(FolderEntity folderEntity) {
        activeFolder = folderEntity;
        listeners.triggerEvent(listener -> listener.activeFolderUpdated(activeFolder));
    }
}
