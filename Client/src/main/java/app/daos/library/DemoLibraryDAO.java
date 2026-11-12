package app.daos.library;

import app.entities.FolderEntity;

public class DemoLibraryDAO implements LibraryDAO {

    /**
     * This function should actually load the full library. In demo mode, it's always made only of the root folder,
     * named after the user's username.
     * @param username in case the user is not logged in, it should be "guest"
     * @return the library's root folder
     */
    @Override
    public FolderEntity loadFullLibrary(String username) {
        return new FolderEntity(username);
    }

    @Override
    public FolderEntity createFolder(String name, FolderEntity parentFolder) {
        return new FolderEntity(name);
    }
}
