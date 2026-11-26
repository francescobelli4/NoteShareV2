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
import utils.LibraryError;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryControllerTest {

    private static LibraryController libraryController;
    private static ExplorerModel mockedExplorerModel;
    private static LibraryModel libraryModel;
    private static DemoLibraryDAO mockedLibraryDAO;
    private static LibraryFolderEntity rootFolder;

    @BeforeAll
    static void beforeAll() {

        UserModel userModel = new UserModel();
        userModel.logIn("username", Role.STUDENT);

        mockedExplorerModel = mock(ExplorerModel.class);
        when(mockedExplorerModel.getMode()).thenReturn(ExplorerMode.LIBRARY);

        libraryModel = new LibraryModel();

        rootFolder = new LibraryFolderEntity("username");
        mockedLibraryDAO = mock(DemoLibraryDAO.class);
        when(mockedLibraryDAO.loadFullLibrary("username")).thenReturn(rootFolder);
        when(mockedLibraryDAO.createFolder(eq("newFolder"), any())).thenReturn(new LibraryFolderEntity("newFolder"));
        when(mockedLibraryDAO.createNote(eq("newNote"), any(), any())).thenReturn(new LibraryNoteEntity("newNote", "newPath"));


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

        libraryController.createNoteInActive("newNote", "newPath");

        assertEquals(1, activeFolder.getNotes().size());
        assertEquals("newNote", activeFolder.getNotes().getFirst().getName());

        verify(mockedExplorerModel, times(2)).setItems(anyList());
    }

    @Test
    void createNoteInActive_Duplicate() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");

        libraryController.setActiveFolder(activeFolder);

        libraryController.createNoteInActive("newNote", "newPath");

        assertThrows(LibraryException.class, () -> libraryController.createNoteInActive("newNote", "newPath"));
    }

    @Test
    void setClipboardItemTest_Success() {
        LibraryNoteEntity selectedItem = new LibraryNoteEntity("name", "path");
        when(mockedExplorerModel.getSelectedItem()).thenReturn(selectedItem);

        libraryController.setClipboardItem();
        assertEquals(selectedItem, libraryModel.getClipboardItem());
    }

    @Test
    void setClipboardItemTest_Null() {
        libraryModel.setClipboardItem(null);
        when(mockedExplorerModel.getSelectedItem()).thenReturn(null);

        libraryController.setClipboardItem();
        assertNull(libraryModel.getClipboardItem());
    }

    @Test
    void pasteClipboardIntoActive_ItemNull() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");

        libraryController.setActiveFolder(activeFolder);
        libraryModel.setClipboardItem(null);
        libraryController.pasteClipboardIntoActive();

        assertEquals(0, activeFolder.getNotes().size());
        assertEquals(0, activeFolder.getSubFolders().size());
    }

    @Test
    void pasteClipboardIntoActive_Folder_Success() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");
        LibraryFolderEntity clipboardItem = new LibraryFolderEntity("name");

        libraryController.setActiveFolder(activeFolder);
        libraryModel.setClipboardItem(clipboardItem);
        libraryController.pasteClipboardIntoActive();

        verify(mockedLibraryDAO).pasteFolder(clipboardItem, activeFolder);
        assertEquals(clipboardItem, activeFolder.getSubFolders().getFirst());
        verify(mockedExplorerModel, times(2)).setItems(anyList());
    }

    @Test
    void pasteClipboardIntoActive_Folder_AlreadyExists() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");
        LibraryFolderEntity clipboardItem = new LibraryFolderEntity("name");

        activeFolder.addSubFolder(clipboardItem);
        libraryController.setActiveFolder(activeFolder);
        libraryModel.setClipboardItem(clipboardItem);

        LibraryException e = assertThrows(LibraryException.class, () -> libraryController.pasteClipboardIntoActive());
        assertEquals(LibraryError.FOLDER_ALREADY_EXISTS, e.getLibraryError());
    }

    @Test
    void pasteClipboardIntoActive_Folder_IsDescendantOf() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");
        LibraryFolderEntity clipboardItem = new LibraryFolderEntity("name");

        activeFolder.addSubFolder(clipboardItem);
        libraryController.setActiveFolder(clipboardItem);
        libraryModel.setClipboardItem(clipboardItem);

        LibraryException e = assertThrows(LibraryException.class, () -> libraryController.pasteClipboardIntoActive());
        assertEquals(LibraryError.PASTE_IN_SAME_OR_DESCENDANT, e.getLibraryError());
    }

    @Test
    void pasteClipboardIntoActive_Note_Success() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");
        LibraryNoteEntity clipboardItem = new LibraryNoteEntity("name", "path");

        libraryController.setActiveFolder(activeFolder);
        libraryModel.setClipboardItem(clipboardItem);
        libraryController.pasteClipboardIntoActive();

        verify(mockedLibraryDAO).pasteNote(clipboardItem, activeFolder);
        assertEquals(clipboardItem, activeFolder.getNotes().getFirst());
        verify(mockedExplorerModel, times(2)).setItems(anyList());
    }

    @Test
    void pasteClipboardIntoActive_Note_AlreadyExists() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");
        LibraryNoteEntity clipboardItem = new LibraryNoteEntity("name", "path");

        activeFolder.addNote(clipboardItem);
        libraryController.setActiveFolder(activeFolder);
        libraryModel.setClipboardItem(clipboardItem);

        LibraryException e = assertThrows(LibraryException.class, () -> libraryController.pasteClipboardIntoActive());
        assertEquals(LibraryError.NOTE_ALREADY_EXISTS, e.getLibraryError());
    }

    @Test
    void deleteSelectedItemTest_Null() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");
        LibraryFolderEntity deletingFolder = new LibraryFolderEntity("deletingFolder");

        activeFolder.addSubFolder(deletingFolder);
        when(mockedExplorerModel.getSelectedItem()).thenReturn(null);

        libraryController.deleteSelectedItem();

        assertEquals(1, activeFolder.getSubFolders().size());
    }

    @Test
    void deleteSelectedItemTest_Folder() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");
        LibraryFolderEntity deletingFolder = new LibraryFolderEntity("deletingFolder");

        activeFolder.addSubFolder(deletingFolder);
        libraryModel.changeActiveFolder(activeFolder);

        when(mockedExplorerModel.getSelectedItem()).thenReturn(deletingFolder);

        libraryController.deleteSelectedItem();

        assertEquals(0, activeFolder.getSubFolders().size());
        verify(mockedExplorerModel).deSelectItem();
        verify(mockedExplorerModel).setItems(anyList());
    }

    @Test
    void deleteSelectedItemTest_Note() {
        LibraryFolderEntity activeFolder = new LibraryFolderEntity("folder");
        LibraryNoteEntity deletingNote = new LibraryNoteEntity("deletingNote", "path");

        activeFolder.addNote(deletingNote);
        libraryModel.changeActiveFolder(activeFolder);

        when(mockedExplorerModel.getSelectedItem()).thenReturn(deletingNote);

        libraryController.deleteSelectedItem();

        assertEquals(0, activeFolder.getNotes().size());
        verify(mockedExplorerModel).deSelectItem();
        verify(mockedExplorerModel).setItems(anyList());
    }
}