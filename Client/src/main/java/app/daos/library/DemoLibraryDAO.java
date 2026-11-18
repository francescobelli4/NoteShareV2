package app.daos.library;

import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;

public class DemoLibraryDAO implements LibraryDAO {

    /**
     * This function should actually load the full library. In demo mode, it's always made only of the root folder,
     * named after the user's username.
     * @param username in case the user is not logged in, it should be "guest"
     * @return the library's root folder
     */
    @Override
    public LibraryFolderEntity loadFullLibrary(String username) {
        return new LibraryFolderEntity(username);
    }

    @Override
    public LibraryFolderEntity createFolder(String name, LibraryFolderEntity parentFolder) {
        return new LibraryFolderEntity(name);
    }

    @Override
    public LibraryNoteEntity createNote(String name, LibraryFolderEntity parentFolder) {
        return new LibraryNoteEntity(name);
    }
}
