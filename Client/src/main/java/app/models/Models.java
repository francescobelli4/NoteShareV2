package app.models;

import app.models.explorermodel.ExplorerModel;
import app.models.notificationmodel.NotificationModel;
import app.models.usermodel.UserModel;
import app.models.walletmodel.WalletModel;

public record Models(UserModel userModel, WalletModel walletModel, ExplorerModel explorerModel, NotificationModel notificationModel) { }
