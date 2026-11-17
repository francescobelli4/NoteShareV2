package app.daos.library;

import app.entities.FolderEntity;
import app.entities.NoteEntity;

public interface LibraryDAO {
    FolderEntity loadFullLibrary(String username);
    FolderEntity createFolder(String name, FolderEntity parentFolder);
    NoteEntity createNote(String name, FolderEntity parentFolder);
}
