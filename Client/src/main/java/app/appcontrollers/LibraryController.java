package app.appcontrollers;

import app.daos.library.LibraryDAO;
import app.entities.ExplorerItemEntity;
import app.entities.LibraryFolderEntity;
import app.entities.LibraryNoteEntity;
import app.exceptions.LibraryException;
import app.models.explorermodel.ExplorerMode;
import app.models.explorermodel.ExplorerModel;
import app.models.librarymodel.LibraryModel;
import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import utils.LibraryError;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an application controller which manages the creation of folders and notes. It should also update
 * explorermodel if the explorer is in library mode (so the user is browsing his library...)
 */
public class LibraryController {

    private final UserModel userModel;
    private final ExplorerModel explorerModel;
    private final LibraryModel libraryModel;
    private final LibraryDAO libraryDAO;

    public LibraryController(UserModel userModel, ExplorerModel explorerModel, LibraryModel libraryModel, LibraryDAO libraryDAO) {
        this.userModel = userModel;
        this.explorerModel = explorerModel;
        this.libraryModel = libraryModel;
        this.libraryDAO = libraryDAO;

        userModel.addListener(new UserModelListener() {

            @Override
            public void onLoggedIn() {
                loadLibrary();
            }

            @Override
            public void onLoggedOut() {
                loadLibrary();
            }
        });
    }

    /**
     * This function should get the root folder, generate the entity and set it as root and active.
     * In demo mode, simply the root folder is created by the DAO.
     */
    public void loadLibrary() {

        LibraryFolderEntity rootFolder = libraryDAO.loadFullLibrary(userModel.isLoggedIn() ? userModel.getUsername() : "ns");
        libraryModel.setRootFolder(rootFolder);
        libraryModel.changeActiveFolder(rootFolder);

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(rootFolder));
    }

    /**
     * This function should set a new active folder and then notify the explorer if needed
     * @param libraryFolderEntity the new active folder
     */
    public void setActiveFolder(LibraryFolderEntity libraryFolderEntity) {

        explorerModel.deSelectItem();
        libraryModel.changeActiveFolder(libraryFolderEntity);

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(libraryFolderEntity));
    }

    /**
     * This function should set the active folder to the actual folder's parent folder and then notify the explorer if
     * needed
     */
    public void goBackToParentFolder() {
        LibraryFolderEntity parentFolder = libraryModel.getActiveFolder().getParentFolder();

        if (parentFolder == null) return;

        libraryModel.changeActiveFolder(parentFolder);

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(libraryModel.getActiveFolder()));
    }

    /**
     * This function should create a new folder in the active folder. It also checks if a folder with that name already
     * exists.
     * In demo mode, creating a new folder only consists in adding that new folder to the active folder's subfolders
     * @param name the new folder's name
     * @throws LibraryException in case a folder with that name already exists
     */
    public void createFolderInActive(String name) throws LibraryException {

        LibraryFolderEntity activeFolder = libraryModel.getActiveFolder();
        LibraryFolderEntity folder = libraryDAO.createFolder(name, libraryModel.getActiveFolder());

        if (activeFolder.hasSubFolderNamed(name)) throw new LibraryException(LibraryError.FOLDER_ALREADY_EXISTS);

        libraryModel.addFolderToActive(folder);

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(libraryModel.getActiveFolder()));
    }

    /**
     * This function should create a new note in the active folder. It also checks if a note with that name already
     * exists.
     * In demo mode, creating a new note only consists in adding that new note to the active folder's notes
     * @param name the new note's name
     * @param path the PDF file path
     * @throws LibraryException in case a note with that name already exists
     */
    public void createNoteInActive(String name, String path) throws LibraryException {

        LibraryFolderEntity activeFolder = libraryModel.getActiveFolder();
        LibraryNoteEntity libraryNoteEntity = libraryDAO.createNote(name, path, activeFolder);

        if (activeFolder.hasNoteNamed(name)) throw new LibraryException(LibraryError.NOTE_ALREADY_EXISTS);

        libraryModel.addNoteToActive(libraryNoteEntity);

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(activeFolder));
    }

    public void setClipboardItem() {

        ExplorerItemEntity selectedItem = explorerModel.getSelectedItem();

        if (selectedItem == null) return;

        libraryModel.setClipboardItem(explorerModel.getSelectedItem());
    }

    public void pasteClipboardIntoActive() throws LibraryException {

        ExplorerItemEntity clipboardItem = libraryModel.getClipboardItem();

        if (clipboardItem == null) return;

        switch (clipboardItem) {
            case LibraryFolderEntity libFolder -> pasteFolderIntoActive(libFolder);
            case LibraryNoteEntity libNote -> pasteNoteIntoActive(libNote);
        }
    }

    private void pasteNoteIntoActive(LibraryNoteEntity noteEntity) throws LibraryException {

        LibraryFolderEntity activeFolder = libraryModel.getActiveFolder();
        if (activeFolder.hasNoteNamed(noteEntity.getName())) throw new LibraryException(LibraryError.NOTE_ALREADY_EXISTS);

        libraryDAO.pasteNote(noteEntity, activeFolder);
        libraryModel.addNoteToActive(noteEntity);

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(activeFolder));
    }

    private void pasteFolderIntoActive(LibraryFolderEntity folderEntity) throws LibraryException {

        LibraryFolderEntity activeFolder = libraryModel.getActiveFolder();
        if (activeFolder.hasSubFolderNamed(folderEntity.getName())) throw new LibraryException(LibraryError.FOLDER_ALREADY_EXISTS);

        // We should check if we are pasting the folder in the same folder or in a descendant to prevent loops
        if (activeFolder.isDescendantOf(folderEntity)) throw new LibraryException(LibraryError.PASTE_IN_SAME_OR_DESCENDANT);

        libraryDAO.pasteFolder(folderEntity, libraryModel.getActiveFolder());
        libraryModel.addFolderToActive(folderEntity);

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(libraryModel.getActiveFolder()));
    }

    public void deleteSelectedItem() {
        ExplorerItemEntity selectedItem = explorerModel.getSelectedItem();

        if (selectedItem == null) return;

        switch (selectedItem) {
            case LibraryFolderEntity libFolder -> deleteFolder(libFolder);
            case LibraryNoteEntity libNote -> deleteNote(libNote);
        }
    }

    private void deleteFolder(LibraryFolderEntity folderEntity) {

        libraryModel.deleteFolderFromActive(folderEntity);
        libraryDAO.deleteFolder(folderEntity);

        explorerModel.deSelectItem();

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(libraryModel.getActiveFolder()));
    }

    private void deleteNote(LibraryNoteEntity noteEntity) {

        libraryModel.deleteNoteFromActive(noteEntity);
        libraryDAO.deleteNote(noteEntity);

        explorerModel.deSelectItem();

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(libraryModel.getActiveFolder()));
    }

    /* UTILS */

    private List<ExplorerItemEntity> toExplorerItemEntityList(LibraryFolderEntity folder) {

        List<ExplorerItemEntity> itemsList = new ArrayList<>();
        itemsList.addAll(folder.getSubFolders());
        itemsList.addAll(folder.getNotes());

        return itemsList;
    }

    private boolean shouldUpdateExplorer() {
        return explorerModel.getMode() == ExplorerMode.LIBRARY;
    }
}
