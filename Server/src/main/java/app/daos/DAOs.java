package app.daos;

import app.daos.user.UserDAO;
import app.daos.wallet.WalletDAO;

import java.util.Objects;

public record DAOs(UserDAO userDAO, WalletDAO walletDAO) {

    public DAOs {
        Objects.requireNonNull(userDAO);
        Objects.requireNonNull(walletDAO);
    }
}
