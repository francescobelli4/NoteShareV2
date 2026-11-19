package app.models.librarymodel;

import app.entities.LibraryFolderEntity;

public interface LibraryModelListener {

    void activeFolderUpdated(LibraryFolderEntity activeFolder);
}
