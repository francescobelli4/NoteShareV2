package app.models.librarymodel;

import app.entities.ExplorerItemEntity;
import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;

public interface LibraryModelListener {

    void activeFolderUpdated(LibraryFolderEntity activeFolder);
    void clipboardSet(ExplorerItemEntity item);
    void clipboardUnset();
    void folderDeleted(LibraryFolderEntity folderEntity);
    void noteDeleted(LibraryNoteEntity noteEntity);
}
