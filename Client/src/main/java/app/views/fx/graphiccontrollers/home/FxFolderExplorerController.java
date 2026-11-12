package app.views.fx.graphiccontrollers.home;

import app.appcontrollers.LibraryController;
import app.exceptions.LibraryException;
import app.views.GraphicController;
import utils.LibraryError;
import utils.LibraryRules;

import java.util.Optional;

public class FxFolderExplorerController implements GraphicController {

    private final LibraryController libraryController;

    public FxFolderExplorerController(LibraryController libraryController) {
        this.libraryController = libraryController;
    }

    public void loadLibrary() {
        libraryController.loadLibrary();
    }

    public void onNewFolderButtonPressed() throws LibraryException {
        String testName = "testName";

        Optional<LibraryError> nameError = LibraryRules.isNameValid(testName);

        if (nameError.isPresent()) {
            throw new LibraryException(nameError.get());
        }

        libraryController.createFolderInActive(testName);
    }
}
