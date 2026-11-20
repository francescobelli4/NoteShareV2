package app.appcontrollers;

import app.daos.library.DemoLibraryDAO;
import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;
import app.exceptions.LibraryException;
import app.models.explorermodel.ExplorerMode;
import app.models.explorermodel.ExplorerModel;
import app.models.librarymodel.LibraryModel;
import app.models.usermodel.UserModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import permissions.Role;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryControllerTest {

    private static LibraryController libraryController;
    private static ExplorerModel mockedExplorerModel;
    private static LibraryModel libraryModel;
    private static LibraryFolderEntity rootFolder;

    @BeforeAll
    static void beforeAll() {

        UserModel userModel = new UserModel();
        userModel.logIn("username", Role.STUDENT);

        mockedExplorerModel = mock(ExplorerModel.class);
        when(mockedExplorerModel.getMode()).thenReturn(ExplorerMode.LIBRARY);

        libraryModel = new LibraryModel();

        rootFolder = new LibraryFolderEntity("username");
        DemoLibraryDAO mockedLibraryDAO = mock(DemoLibraryDAO.class);
        when(mockedLibraryDAO.loadFullLibrary("username")).thenReturn(rootFolder);
        when(mockedLibraryDAO.createFolder(eq("newFolder"), any())).thenReturn(new LibraryFolderEntity("newFolder"));
        when(mockedLibraryDAO.createNote(eq("newNote"), any())).thenReturn(new LibraryNoteEntity("newNote"));


        libraryController = new LibraryController(userModel, mockedExplorerModel, libraryModel, mockedLibraryDAO);
    }

    @BeforeEach
    void setUp() {
        clearInvocations(mockedExplorerModel);
    }

    @Test
    void loadLibrary() {
        libraryController.loadLibrary();

        assertEquals(rootFolder, libraryModel.getRootFolder());
        assertEquals(rootFolder, libraryModel.getActiveFolder());
        verify(mockedExplorerModel, times(1)).setItems(anyList());
    }

    @Test
    void setActiveFolder() {
        LibraryFolderEntity newActiveFolder = new LibraryFolderEntity("test");
        libraryController.setActiveFolder(newActiveFolder);

        assertEquals(newActiveFolder, libraryModel.getActiveFolder());

        verify(mockedExplorerModel, times(1)).setItems(anyList());
    }

    @Test
    void goBackToParentFolder() {

        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");
        LibraryFolderEntity parentFolder = new LibraryFolderEntity("parent");
        parentFolder.addSubFolder(activeFolder);

        libraryController.setActiveFolder(activeFolder);
        libraryController.goBackToParentFolder();

        assertEquals(parentFolder, libraryModel.getActiveFolder());
        verify(mockedExplorerModel, times(2)).setItems(anyList());
    }

    @Test
    void goBackToParentFolder_NoParentFolder() {

        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");

        libraryController.setActiveFolder(activeFolder);
        libraryController.goBackToParentFolder();

        assertEquals(activeFolder, libraryModel.getActiveFolder());
    }

    @Test
    void createFolderInActive() {

        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");

        libraryController.setActiveFolder(activeFolder);

        libraryController.createFolderInActive("newFolder");

        assertEquals(1, activeFolder.getSubFolders().size());
        assertEquals("newFolder", activeFolder.getSubFolders().getFirst().getName());

        verify(mockedExplorerModel, times(2)).setItems(anyList());
    }

    @Test
    void createFolderInActive_Duplicate() {

        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");

        libraryController.setActiveFolder(activeFolder);

        libraryController.createFolderInActive("newFolder");

        assertThrows(LibraryException.class, () -> libraryController.createFolderInActive("newFolder"));
    }

    @Test
    void createNoteInActive() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");

        libraryController.setActiveFolder(activeFolder);

        libraryController.createNoteInActive("newNote");

        assertEquals(1, activeFolder.getNotes().size());
        assertEquals("newNote", activeFolder.getNotes().getFirst().getName());

        verify(mockedExplorerModel, times(2)).setItems(anyList());
    }

    @Test
    void createNoteInActive_Duplicate() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");

        libraryController.setActiveFolder(activeFolder);

        libraryController.createNoteInActive("newNote");

        assertThrows(LibraryException.class, () -> libraryController.createNoteInActive("newNote"));
    }
}