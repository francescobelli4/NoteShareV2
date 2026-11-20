package app.daos.library;

import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;

public interface LibraryDAO {
    LibraryFolderEntity loadFullLibrary(String username);
    LibraryFolderEntity createFolder(String name, LibraryFolderEntity parentFolder);
    LibraryNoteEntity createNote(String name, String path, LibraryFolderEntity parentFolder);
}
