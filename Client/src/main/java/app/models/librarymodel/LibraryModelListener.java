package app.models.librarymodel;

import app.entities.FolderEntity;

public interface LibraryModelListener {
    void activeFolderUpdated(FolderEntity folderEntity);
}
