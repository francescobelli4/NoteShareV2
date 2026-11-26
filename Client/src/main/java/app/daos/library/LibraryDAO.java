package app.daos.library;

import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;

public interface LibraryDAO {
    LibraryFolderEntity loadFullLibrary(String username);
    LibraryFolderEntity createFolder(String name, LibraryFolderEntity parentFolder);
    LibraryNoteEntity createNote(String name, String path, LibraryFolderEntity parentFolder);

    void pasteFolder(LibraryFolderEntity folder, LibraryFolderEntity parentFolder);
    void pasteNote(LibraryNoteEntity note, LibraryFolderEntity parentFolder);
    void deleteFolder(LibraryFolderEntity folderEntity);
    void deleteNote(LibraryNoteEntity noteEntity);
}
