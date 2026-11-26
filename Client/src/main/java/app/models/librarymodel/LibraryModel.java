package app.models.librarymodel;

import app.entities.ExplorerItemEntity;
import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;
import app.utils.ListenerHelper;
import app.utils.Subscription;

/**
 * This model represents the user's library in the app. We can add or remove notes and folders in the library and
 * the listeners should be notified when a folder is updated.
 */
public class LibraryModel {

    private final ListenerHelper<LibraryModelListener> listeners = new ListenerHelper<>();

    private LibraryFolderEntity rootFolder;
    private LibraryFolderEntity activeFolder;
    private ExplorerItemEntity clipboardItem;

    public Subscription addListener(LibraryModelListener listener) {
        return listeners.subscribe(listener);
    }

    public void setRootFolder(LibraryFolderEntity libraryFolderEntity) {
        rootFolder = libraryFolderEntity;
    }

    public void changeActiveFolder(LibraryFolderEntity libraryFolderEntity) {
        activeFolder = libraryFolderEntity;
        clipboardItem = null;

        listeners.triggerEvent(listener -> listener.activeFolderUpdated(activeFolder));
    }

    public void addFolderToActive(LibraryFolderEntity libraryFolderEntity) {
        activeFolder.addSubFolder(libraryFolderEntity);
        listeners.triggerEvent(listener -> listener.activeFolderUpdated(activeFolder));
    }

    public void addNoteToActive(LibraryNoteEntity libraryNoteEntity) {
        activeFolder.addNote(libraryNoteEntity);
        listeners.triggerEvent(listener -> listener.activeFolderUpdated(activeFolder));
    }

    public void setClipboardItem(ExplorerItemEntity item) {
        clipboardItem = item;
        listeners.triggerEvent(l -> l.clipboardSet(item));
    }

    public void deleteFolderFromActive(LibraryFolderEntity libraryFolderEntity) {

        if (clipboardItem == libraryFolderEntity) {
            clipboardItem = null;
            listeners.triggerEvent(LibraryModelListener::clipboardUnset);
        }

        activeFolder.deleteSubFolder(libraryFolderEntity);
        listeners.triggerEvent(l -> l.folderDeleted(libraryFolderEntity));
    }

    public void deleteNoteFromActive(LibraryNoteEntity libraryNoteEntity) {
        if (clipboardItem == libraryNoteEntity) {
            clipboardItem = null;
            listeners.triggerEvent(LibraryModelListener::clipboardUnset);
        }

        activeFolder.deleteNote(libraryNoteEntity);
        listeners.triggerEvent(l -> l.noteDeleted(libraryNoteEntity));
    }

    public ExplorerItemEntity getClipboardItem() {
        return clipboardItem;
    }

    public LibraryFolderEntity getActiveFolder() {
        return activeFolder;
    }

    public LibraryFolderEntity getRootFolder() {
        return rootFolder;
    }
}
