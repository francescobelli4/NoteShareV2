package app.appcontrollers;

import app.models.librarymodel.LibraryModel;

public record AppControllers(AuthController authController, ProfileController profileController, LibraryController libraryController) {
}
