package app.models.librarymodel;

import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LibraryModelTest {

    private static final LibraryModel libraryModel = new LibraryModel();
    private static LibraryModelListener libraryModelListener;

    private final LibraryFolderEntity rootFolder = new LibraryFolderEntity("rootFolder");

    @BeforeAll
    static void beforeAll() {
        libraryModelListener = mock(LibraryModelListener.class);
        libraryModel.addListener(libraryModelListener);
    }

    @BeforeEach
    void resetInvocations() {
        clearInvocations(libraryModelListener);
    }

    @Test
    void setRootFolder() {
        libraryModel.setRootFolder(rootFolder);
        assertEquals(rootFolder, libraryModel.getRootFolder());
    }

    @Test
    void changeActiveFolder() {
        LibraryFolderEntity newActiveFolder = new LibraryFolderEntity("newActiveFolder");
        libraryModel.changeActiveFolder(newActiveFolder);

        assertEquals(newActiveFolder, libraryModel.getActiveFolder());
        verify(libraryModelListener).activeFolderUpdated(any(LibraryFolderEntity.class));
    }

    @Test
    void addFolderToActive() {
        LibraryFolderEntity newActiveFolder = new LibraryFolderEntity("newActiveFolder");
        libraryModel.changeActiveFolder(newActiveFolder);

        LibraryFolderEntity childFolder = new LibraryFolderEntity("childFolder");
        libraryModel.addFolderToActive(childFolder);

        assertEquals(1, newActiveFolder.getSubFolders().size());
        assertEquals("childFolder", newActiveFolder.getSubFolders().getFirst().getName());

        verify(libraryModelListener, times(2)).activeFolderUpdated(any(LibraryFolderEntity.class));
    }

    @Test
    void addNoteToActive() {
        LibraryFolderEntity newActiveFolder = new LibraryFolderEntity("newActiveFolder");
        libraryModel.changeActiveFolder(newActiveFolder);

        LibraryNoteEntity childNote = new LibraryNoteEntity("childNote", "childPath");
        libraryModel.addNoteToActive(childNote);

        assertEquals(1, newActiveFolder.getNotes().size());
        assertEquals("childNote", newActiveFolder.getNotes().getFirst().getName());

        verify(libraryModelListener, times(2)).activeFolderUpdated(any(LibraryFolderEntity.class));
    }
}