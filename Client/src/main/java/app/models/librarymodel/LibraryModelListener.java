package app.models.librarymodel;

import app.entities.ExplorerItemEntity;
import app.entities.LibraryFolderEntity;

public interface LibraryModelListener {

    void activeFolderUpdated(LibraryFolderEntity activeFolder);
    void clipboardUpdated(ExplorerItemEntity item);
}
