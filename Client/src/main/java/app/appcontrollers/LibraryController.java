package app.appcontrollers;

import app.daos.library.LibraryDAO;
import app.entities.FolderEntity;
import app.models.librarymodel.LibraryModel;
import app.models.usermodel.UserModel;

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

    public void createFolder(String name) {
        FolderEntity folder = libraryDAO.createFolder(name);
        libraryModel.addFolderToActive(folder);
    }
}
