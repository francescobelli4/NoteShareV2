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

    /**
     * This function should update the active folder and notify the listeners.
     * @param libraryFolderEntity the new active folder
     */
    public void changeActiveFolder(LibraryFolderEntity libraryFolderEntity) {
        activeFolder = libraryFolderEntity;

        listeners.triggerEvent(listener -> listener.activeFolderUpdated(activeFolder));
    }

    /**
     * This function should add a subfolder to the active folder and notify the listeners.
     * @param libraryFolderEntity the folder that has to be added to the active folder
     */
    public void addFolderToActive(LibraryFolderEntity libraryFolderEntity) {
        activeFolder.addSubFolder(libraryFolderEntity);
        listeners.triggerEvent(listener -> listener.activeFolderUpdated(activeFolder));
    }

    /**
     * This function should add a note to the active folder and notify the listeners.
     * @param libraryNoteEntity the note that has to be added to the active folder
     */
    public void addNoteToActive(LibraryNoteEntity libraryNoteEntity) {
        activeFolder.addNote(libraryNoteEntity);
        listeners.triggerEvent(listener -> listener.activeFolderUpdated(activeFolder));
    }

    /**
     * This function should set the clipboard item and notify the listeners
     * @param item the copied item
     */
    public void setClipboardItem(ExplorerItemEntity item) {
        clipboardItem = item;
        listeners.triggerEvent(l -> l.clipboardSet(item));
    }

    /**
     * This function should delete a folder from the active folder and notify the listeners. In case the clipboard item
     * is inside that folder, we should also clear the clipboard.
     * @param libraryFolderEntity the folder that has to be deleted.
     */
    public void deleteFolderFromActive(LibraryFolderEntity libraryFolderEntity) {

        // When deleting a folder, we should check if clipboardItem is inside that folder (at any depth) and unset it
        if (clipboardItem != null && isInside(clipboardItem, libraryFolderEntity)) clearClipboard();

        activeFolder.deleteSubFolder(libraryFolderEntity);
        listeners.triggerEvent(l -> l.folderDeleted(libraryFolderEntity));
    }

    /**
     * This function should delete a note from the active folder and notify the listeners.
     * @param libraryNoteEntity the note that has to be deleted.
     */
    public void deleteNoteFromActive(LibraryNoteEntity libraryNoteEntity) {

        if (clipboardItem == libraryNoteEntity) clearClipboard();

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

    private void clearClipboard() {
        clipboardItem = null;
        listeners.triggerEvent(LibraryModelListener::clipboardUnset);
    }

    private boolean isInside(ExplorerItemEntity checkingItem, LibraryFolderEntity deletingFolder) {
        return switch (checkingItem) {
            case LibraryFolderEntity checkingFolder -> checkingFolder.isDescendantOf(deletingFolder);
            case LibraryNoteEntity checkingNote -> checkingNote.getParentFolder().isDescendantOf(deletingFolder);
        };
    }
}
