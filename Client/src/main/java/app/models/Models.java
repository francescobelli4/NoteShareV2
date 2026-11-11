package app.models;

import app.models.librarymodel.LibraryModel;
import app.models.usermodel.UserModel;
import app.models.walletmodel.WalletModel;

public record Models(UserModel userModel, WalletModel walletModel, LibraryModel libraryModel) { }
