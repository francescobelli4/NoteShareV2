package app.models.explorermodel;

import app.entities.FolderEntity;

public interface ExplorerModelListener {
    void activeFolderUpdated(FolderEntity folderEntity);
}
