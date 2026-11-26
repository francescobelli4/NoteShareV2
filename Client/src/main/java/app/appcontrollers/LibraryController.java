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

    /**
     * This function should set the explorer's selected item as the clipboard item when the user copies a note or a
     * folder.
     */
    public void setClipboardItem() {

        ExplorerItemEntity selectedItem = explorerModel.getSelectedItem();

        if (selectedItem == null) return;

        libraryModel.setClipboardItem(explorerModel.getSelectedItem());
    }

    /**
     * This function should paste the clipboard item (a folder or a note) in the active folder
     * @throws LibraryException in case the item already exists or the user is trying to paste a folder in a descendant.
     */
    public void pasteClipboardIntoActive() throws LibraryException {

        ExplorerItemEntity clipboardItem = libraryModel.getClipboardItem();

        if (clipboardItem == null) return;

        switch (clipboardItem) {
            case LibraryFolderEntity libFolder -> pasteFolderIntoActive(libFolder);
            case LibraryNoteEntity libNote -> pasteNoteIntoActive(libNote);
        }
    }

    /**
     * This function should paste a note in the active folder. Simply it adds the note in the active folder if another
     * note with the same name does not exist in the active folder.
     * @param noteEntity the note that the user wants to paste
     * @throws LibraryException in case a note with that name already exists
     */
    private void pasteNoteIntoActive(LibraryNoteEntity noteEntity) throws LibraryException {

        LibraryFolderEntity activeFolder = libraryModel.getActiveFolder();
        if (activeFolder.hasNoteNamed(noteEntity.getName())) throw new LibraryException(LibraryError.NOTE_ALREADY_EXISTS);

        libraryDAO.pasteNote(noteEntity, activeFolder);
        libraryModel.addNoteToActive(noteEntity);

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(activeFolder));
    }

    /**
     * This function should paste a folder in the active folder. It should only add that folder to the active folder if
     * active folder is not a descendant of the folder the user wants to past (otherwise it would create a loop!) and
     * there isn't any other folder with that name in the active folder.
     * @param folderEntity the folder the user wants to paste.
     * @throws LibraryException in case a folder with that name already exists or active folder is a descendant of the
     * folder the user wants to paste.
     */
    private void pasteFolderIntoActive(LibraryFolderEntity folderEntity) throws LibraryException {

        LibraryFolderEntity activeFolder = libraryModel.getActiveFolder();
        if (activeFolder.hasSubFolderNamed(folderEntity.getName())) throw new LibraryException(LibraryError.FOLDER_ALREADY_EXISTS);

        // We should check if we are pasting the folder in the same folder or in a descendant to prevent loops
        if (activeFolder.isDescendantOf(folderEntity)) throw new LibraryException(LibraryError.PASTE_IN_SAME_OR_DESCENDANT);

        libraryDAO.pasteFolder(folderEntity, libraryModel.getActiveFolder());
        libraryModel.addFolderToActive(folderEntity);

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(libraryModel.getActiveFolder()));
    }

    /**
     * This function should delete the selected item (a folder or a note).
     */
    public void deleteSelectedItem() {
        ExplorerItemEntity selectedItem = explorerModel.getSelectedItem();

        if (selectedItem == null) return;

        switch (selectedItem) {
            case LibraryFolderEntity libFolder -> deleteFolder(libFolder);
            case LibraryNoteEntity libNote -> deleteNote(libNote);
        }
    }

    /**
     * This function should delete a folder and clear explorer model's selected item.
     * @param folderEntity the folder the user wants to delete.
     */
    private void deleteFolder(LibraryFolderEntity folderEntity) {

        libraryModel.deleteFolderFromActive(folderEntity);
        libraryDAO.deleteFolder(folderEntity);

        explorerModel.deSelectItem();

        if (shouldUpdateExplorer()) explorerModel.setItems(toExplorerItemEntityList(libraryModel.getActiveFolder()));
    }

    /**
     * This function should delete a note and clear the explorer model's selected item.
     * @param noteEntity the note the user wants to delete.
     */
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
