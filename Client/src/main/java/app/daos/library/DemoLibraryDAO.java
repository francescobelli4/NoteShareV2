package app.daos.library;

import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;

import java.util.Objects;

public class DemoLibraryDAO implements LibraryDAO {

    // In DEMO mode, these variables are needed to simulate the persistence of folders
    private LibraryFolderEntity rootFolder;
    private String username;

    /**
     * This function should load the full library. In demo mode, it's always made only of the root folder,
     * named after the user's username.
     * @param username in case the user is not logged in, it should be "guest"
     * @return the library's root folder
     */
    @Override
    public LibraryFolderEntity loadFullLibrary(String username) {
        if (rootFolder == null || !Objects.equals(this.username, username)) {
            this.username = username;
            rootFolder = new LibraryFolderEntity(username);
        }
        return rootFolder;
    }

    /**
     * This function should create a new folder (IN PERSISTENT MEMORY) in a specified parent folder. In demo mode,
     * persistent memory is not used, so it should only return a new LibraryFolderEntity
     * @param name the new folder's name
     * @param parentFolder the new folder's parent folder
     * @return the LibraryFolderEntity associated with the new folder that was created.
     */
    @Override
    public LibraryFolderEntity createFolder(String name, LibraryFolderEntity parentFolder) {
        return new LibraryFolderEntity(name);
    }

    /**
     * This function should create a new note (IN PERSISTENT MEMORY) in a specified parent folder. In demo mode,
     * persistent memory is not used, so it should only return a new LibraryNoteEntity
     * @param name the new note's name
     * @param path the actual PDF path
     * @param parentFolder the new note's parent folder
     * @return the LibraryNoteEntity associated with the new note that was created.
     */
    @Override
    public LibraryNoteEntity createNote(String name, String path, LibraryFolderEntity parentFolder) {
        return new LibraryNoteEntity(name, path);
    }

    @Override
    public void pasteFolder(LibraryFolderEntity folder, LibraryFolderEntity parentFolder) {
        // No-op in DEMO
    }

    @Override
    public void pasteNote(LibraryNoteEntity note, LibraryFolderEntity parentFolder) {
        // No-op in DEMO
    }

    @Override
    public void deleteFolder(LibraryFolderEntity folderEntity) {
        // No-op in DEMO
    }

    @Override
    public void deleteNote(LibraryNoteEntity noteEntity) {
        // No-op in DEMO
    }
}
