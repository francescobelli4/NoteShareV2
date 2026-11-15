package app.views.fx.graphiccontrollers.home;

import app.appcontrollers.LibraryController;
import app.appcontrollers.NotificationController;
import app.exceptions.LibraryException;
import app.views.GraphicController;
import utils.LibraryError;
import utils.LibraryRules;

import java.util.Optional;

public class FxFolderExplorerController implements GraphicController {

    private final LibraryController libraryController;
    private final NotificationController notificationController;

    public FxFolderExplorerController(LibraryController libraryController, NotificationController notificationController) {
        this.libraryController = libraryController;
        this.notificationController = notificationController;
    }

    public void loadLibrary() {
        libraryController.loadLibrary();
    }

    public void onNewFolderButtonPressed() throws LibraryException {
        String testName = "testName";

        notificationController.showError("titoloo", "descrizioneeeeeeeee");

        Optional<LibraryError> nameError = LibraryRules.isNameValid(testName);

        if (nameError.isPresent()) {
            throw new LibraryException(nameError.get());
        }

        libraryController.createFolderInActive(testName);
    }
}
