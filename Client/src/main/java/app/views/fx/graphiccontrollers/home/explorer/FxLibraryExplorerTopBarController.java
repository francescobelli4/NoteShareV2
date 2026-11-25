package app.views.fx.graphiccontrollers.home.explorer;

import app.appcontrollers.LibraryController;
import app.appcontrollers.NotificationController;
import app.exceptions.LibraryException;
import app.utils.Locales;
import app.utils.dialogs.DialogSpecs;
import app.views.GraphicController;
import app.views.fx.FxNavigator;
import app.views.fx.FxViewFactory;
import app.views.fx.pages.dialog.FxDialogView;
import utils.LibraryError;
import utils.LibraryRules;

import java.util.Optional;

public class FxLibraryExplorerTopBarController implements GraphicController {

    private final LibraryController libraryController;
    private final NotificationController notificationController;
    private final FxViewFactory factory;
    private final FxNavigator navigator;

    public FxLibraryExplorerTopBarController(LibraryController libraryController, NotificationController notificationController, FxViewFactory factory, FxNavigator navigator) {
        this.libraryController = libraryController;
        this.notificationController = notificationController;
        this.factory = factory;
        this.navigator = navigator;
    }

    public void loadLibrary() {
        libraryController.loadLibrary();
    }

    public void onNewNoteButtonPressed() {
        FxDialogView dialog = factory.createFxDialogView();
        DialogSpecs dialogSpecs = new DialogSpecs();
        dialogSpecs.addTextField("name", Locales.get("dialog.note_name"));
        dialogSpecs.addFileChooser("chosen_file");

        dialog.populate(Locales.get("dialog.create_new_note"), dialogSpecs, output -> {

            String name = output.get("name");
            String chosenFile = output.get("chosen_file");

            Optional<LibraryError> nameError = LibraryRules.isNameValid(name);

            if (nameError.isPresent()) {

                String errorStr = switch (nameError.get()) {
                    case NAME_EMPTY -> Locales.get("dialog.name_empty");
                    case NAME_TOO_LONG -> Locales.get("dialog.name_too_long", name);
                    case NAME_NOT_VALID -> Locales.get("dialog.name_not_valid", name);
                    default -> "Unexpected error";
                };

                notificationController.showError(Locales.get("error"), errorStr);
                return;
            }

            if (chosenFile == null) {
                notificationController.showError(Locales.get("error"), Locales.get("dialog.file_not_chosen"));
                return;
            }

            try {
                libraryController.createNoteInActive(name, chosenFile);
                notificationController.showSuccess(Locales.get("success"), Locales.get("dialog.note_created", name));
            } catch (LibraryException _) {
                notificationController.showError(Locales.get("error"), Locales.get("dialog.note_already_exists"));
            }
        });

        navigator.showOverlay(dialog);
    }

    public void onNewFolderButtonPressed() {

        FxDialogView dialog = factory.createFxDialogView();
        DialogSpecs dialogSpecs = new DialogSpecs();
        dialogSpecs.addTextField("name", Locales.get("dialog.folder_name"));

        dialog.populate(Locales.get("dialog.create_new_folder"), dialogSpecs, output -> {

            String name = output.get("name");

            Optional<LibraryError> nameError = LibraryRules.isNameValid(name);

            if (nameError.isPresent()) {

                String errorStr = switch (nameError.get()) {
                    case NAME_EMPTY -> Locales.get("dialog.name_empty");
                    case NAME_TOO_LONG -> Locales.get("dialog.name_too_long", name);
                    case NAME_NOT_VALID -> Locales.get("dialog.name_not_valid", name);
                    default -> "Unexpected error";
                };

                notificationController.showError(Locales.get("error"), errorStr);
                return;
            }

            try {
                libraryController.createFolderInActive(name);
                notificationController.showSuccess(Locales.get("success"), Locales.get("dialog.folder_created", name));
            } catch (LibraryException _) {
                notificationController.showError(Locales.get("error"), Locales.get("dialog.folder_already_exists", name));
            }
        });

        navigator.showOverlay(dialog);
    }

    public void onBackButtonClicked() {
        libraryController.goBackToParentFolder();
    }

    public void onCopyButtonPressed() {
        libraryController.setClipboardItem();
    }

    public void onPasteButtonPressed() {

        try {
            libraryController.pasteClipboardIntoActive();
        } catch (LibraryException _) {

        }
    }
}
