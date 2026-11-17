package app.daos.library;

import app.entities.FolderEntity;

public interface LibraryDAO {
    FolderEntity loadFullLibrary(String username);
    FolderEntity createFolder(String name, FolderEntity parentFolder);
}
