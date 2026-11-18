package app.appcontrollers;

import app.daos.library.LibraryDAO;
import app.entities.FolderEntity;
import app.entities.NoteEntity;
import app.exceptions.LibraryException;
import app.models.explorermodel.ExplorerModel;
import app.models.usermodel.UserModel;
import utils.LibraryError;
import utils.LibraryRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is an application controller which manages the creation of folders and notes.
 */
public class LibraryController {

    private final UserModel userModel;
    private final ExplorerModel explorerModel;
    private final LibraryDAO libraryDAO;

    public LibraryController(UserModel userModel, ExplorerModel explorerModel, LibraryDAO libraryDAO) {
        this.userModel = userModel;
        this.explorerModel = explorerModel;
        this.libraryDAO = libraryDAO;
    }

    public void loadLibrary() {
        explorerModel.setRootFolder(libraryDAO.loadFullLibrary(userModel.isLoggedIn() ? userModel.getUsername() : "ns"));
    }

    public void setActiveFolder(FolderEntity folderEntity) {
        explorerModel.updateActiveFolder(folderEntity);
    }

    public void goBackToParentFolder() {
        explorerModel.updateActiveFolder(explorerModel.getActiveFolder().getParentFolder());
    }

    public void createFolderInActive(String name) throws LibraryException {
        FolderEntity folder = libraryDAO.createFolder(name, explorerModel.getActiveFolder());

        List<String> siblings = new ArrayList<>();
        explorerModel.getActiveFolder().getSubFolders().forEach(folderEntity -> siblings.add(folderEntity.getName()));
        Optional<LibraryError> duplicateError = LibraryRules.isDuplicate(name, siblings);

        if (duplicateError.isPresent()) {
            throw new LibraryException(duplicateError.get());
        }

        explorerModel.addFolderToActive(folder);
    }

    public void createNoteInActive(String name) throws LibraryException {
        NoteEntity noteEntity = libraryDAO.createNote(name, explorerModel.getActiveFolder());

        List<String> siblings = new ArrayList<>();
        explorerModel.getActiveFolder().getNotes().forEach(folderEntity -> siblings.add(folderEntity.getName()));
        Optional<LibraryError> duplicateError = LibraryRules.isDuplicate(name, siblings);

        if (duplicateError.isPresent()) {
            throw new LibraryException(duplicateError.get());
        }

        explorerModel.addNoteToActive(noteEntity);
    }
}
