package app.daos.wallet;

import app.entities.WalletEntity;

public interface WalletDAO {

    WalletEntity updateWalletBalance(WalletEntity walletEntity, int balance);

    boolean saveWallet(WalletEntity walletEntity);

    int getWalletBalanceByUsername(String username);
}