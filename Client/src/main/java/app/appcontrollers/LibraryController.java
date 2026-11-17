package app.appcontrollers;

import app.daos.library.LibraryDAO;
import app.entities.FolderEntity;
import app.exceptions.LibraryException;
import app.models.librarymodel.LibraryModel;
import app.models.usermodel.UserModel;
import utils.LibraryError;
import utils.LibraryRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryController {

    private final UserModel userModel;
    private final LibraryModel libraryModel;
    private final LibraryDAO libraryDAO;

    public LibraryController(UserModel userModel, LibraryModel libraryModel, LibraryDAO libraryDAO) {
        this.userModel = userModel;
        this.libraryModel = libraryModel;
        this.libraryDAO = libraryDAO;
    }

    public void loadLibrary() {
        libraryModel.setRootFolder(libraryDAO.loadFullLibrary(userModel.isLoggedIn() ? userModel.getUsername() : "ns"));
    }

    public void createFolderInActive(String name) throws LibraryException {
        FolderEntity folder = libraryDAO.createFolder(name, libraryModel.getActiveFolder());

        List<String> siblings = new ArrayList<>();
        libraryModel.getActiveFolder().getSubFolders().forEach(folderEntity -> siblings.add(folderEntity.getName()));
        Optional<LibraryError> duplicateError = LibraryRules.isDuplicate(name, siblings);

        if (duplicateError.isPresent()) {
            throw new LibraryException(duplicateError.get());
        }

        libraryModel.addFolderToActive(folder);
    }
}
