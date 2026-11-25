package app.daos.library;

import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemoLibraryDAOTest {

    private final DemoLibraryDAO demoLibraryDAO = new DemoLibraryDAO();

    @Test
    void loadFullLibrary() {
        LibraryFolderEntity rootFolder = demoLibraryDAO.loadFullLibrary("username");
        assertNotNull(rootFolder);
        assertEquals("username", rootFolder.getName());
    }

    @Test
    void createFolder() {
        LibraryFolderEntity newFolder = demoLibraryDAO.createFolder("testFolder", new LibraryFolderEntity("dcc"));
        assertNotNull(newFolder);
        assertEquals("testFolder", newFolder.getName());
    }

    @Test
    void createNote() {
        LibraryNoteEntity newNote = demoLibraryDAO.createNote("testNote", "testPath", new LibraryFolderEntity("dcc"));
        assertNotNull(newNote);
        assertEquals("testNote", newNote.getName());
    }
}