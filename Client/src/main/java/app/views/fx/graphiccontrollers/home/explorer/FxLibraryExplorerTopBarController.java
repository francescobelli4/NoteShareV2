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
import errors.LibraryError;
import utils.LibraryRules;

import java.util.Optional;

public class FxLibraryExplorerTopBarController implements GraphicController {

    private final LibraryController libraryController;
    private final NotificationController notificationController;
    private final FxViewFactory factory;
    private final FxNavigator navigator;

    private final String errorTitle = Locales.get("error");

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
                notificationController.showError(errorTitle, Locales.getErrorLabel(nameError.get(), name));
                return;
            }

            if (chosenFile == null) {
                notificationController.showError(errorTitle, Locales.get("dialog.file_not_chosen"));
                return;
            }

            try {
                libraryController.createNoteInActive(name, chosenFile);
                notificationController.showSuccess(Locales.get("success"), Locales.get("dialog.note_created", name));
            } catch (LibraryException libraryException) {
                notificationController.showError(errorTitle, Locales.getErrorLabel(libraryException.getLibraryError(), name));
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
                notificationController.showError(errorTitle, Locales.getErrorLabel(nameError.get(), name));
                return;
            }

            try {
                libraryController.createFolderInActive(name);
                notificationController.showSuccess(Locales.get("success"), Locales.get("dialog.folder_created", name));
            } catch (LibraryException libraryException) {
                notificationController.showError(errorTitle,  Locales.getErrorLabel(libraryException.getLibraryError(), name));
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
        } catch (LibraryException libraryException) {
            notificationController.showError(errorTitle, Locales.getErrorLabel(libraryException.getLibraryError(), libraryException.getCausingItemName()));
        }
    }

    public void onDeleteButtonPressed() {
        libraryController.deleteSelectedItem();
    }
}
