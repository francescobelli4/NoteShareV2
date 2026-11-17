package app.views.fx.graphiccontrollers.home;

import app.appcontrollers.LibraryController;
import app.appcontrollers.NotificationController;
import app.exceptions.LibraryException;
import app.utils.dialogs.DialogSpecs;
import app.views.GraphicController;
import app.views.fx.FxNavigator;
import app.views.fx.FxViewFactory;
import app.views.fx.pages.dialog.FxDialogView;
import utils.LibraryError;
import utils.LibraryRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FxFolderExplorerController implements GraphicController {

    private final LibraryController libraryController;
    private final NotificationController notificationController;
    private final FxViewFactory factory;
    private final FxNavigator navigator;

    public FxFolderExplorerController(LibraryController libraryController, NotificationController notificationController, FxViewFactory factory, FxNavigator navigator) {
        this.libraryController = libraryController;
        this.notificationController = notificationController;
        this.factory = factory;
        this.navigator = navigator;
    }

    public void loadLibrary() {
        libraryController.loadLibrary();
    }

    public void onNewFolderButtonPressed() throws LibraryException {

        FxDialogView dialog = factory.createFxDialogView();
        DialogSpecs dialogSpecs = new DialogSpecs();
        dialogSpecs.addTextField("name", "Folder's name");

        dialog.populate(dialogSpecs, output -> {

            String name = output.get("name");

            Optional<LibraryError> nameError = LibraryRules.isNameValid(name);

            if (nameError.isPresent()) {

                String errorStr = switch (nameError.get()) {
                    case NAME_EMPTY -> "Name can't be empty!";
                    case NAME_TOO_LONG -> "Name %s is too long";
                    case NAME_NOT_VALID -> "Name %s is not valid";
                    default -> "Unexpected error";
                };

                notificationController.showError("Error", String.format(errorStr, name));
                return;
            }

            try {
                libraryController.createFolderInActive(name);
                notificationController.showSuccess("Success", String.format("Folder %s was created.", name));
            } catch (LibraryException e) {
                notificationController.showError("Error", String.format("The folder %s already exists!", name));
            }
        });

        navigator.showOverlay(dialog);
    }
}
