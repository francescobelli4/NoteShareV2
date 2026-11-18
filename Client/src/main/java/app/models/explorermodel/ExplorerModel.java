package app.models.explorermodel;

import app.entities.FolderEntity;
import app.entities.NoteEntity;
import app.utils.ListenerHelper;
import app.utils.Subscription;

public class ExplorerModel {

    private final ListenerHelper<ExplorerModelListener> listeners = new ListenerHelper<>();

    private FolderEntity rootFolder;
    private FolderEntity activeFolder;

    public Subscription addListener(ExplorerModelListener listener) {
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

    public void addFolderToActive(FolderEntity folderEntity) {
        activeFolder.addSubFolder(folderEntity);
        listeners.triggerEvent(listener -> listener.activeFolderUpdated(activeFolder));
    }

    public void addNoteToActive(NoteEntity noteEntity) {
        activeFolder.addNote(noteEntity);
        listeners.triggerEvent(listener -> listener.activeFolderUpdated(activeFolder));
    }

    public FolderEntity getActiveFolder() {
        return activeFolder;
    }
}
