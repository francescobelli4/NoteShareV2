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

        Optional<LibraryError> nameError = LibraryRules.isNameValid(testName);

        if (nameError.isPresent()) {

            String errorStr = switch (nameError.get()) {
                case NAME_EMPTY -> "Name can't be empty!";
                case NAME_TOO_LONG -> "Name %s is too long";
                case NAME_NOT_VALID -> "Name %s is not valid";
                default -> "Unexpected error";
            };

            notificationController.showError("Error", String.format(errorStr, testName));
            throw new LibraryException(nameError.get());
        }

        try {
            libraryController.createFolderInActive(testName);
            notificationController.showSuccess("Success", String.format("Folder %s was created.", testName));
        } catch (LibraryException e) {
            notificationController.showError("Error", String.format("The folder %s already exists!", testName));
            throw e;
        }
    }
}
