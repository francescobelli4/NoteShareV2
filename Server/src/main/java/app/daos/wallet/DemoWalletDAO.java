package app.daos.wallet;

import app.entities.WalletEntity;

import java.util.HashMap;
import java.util.Map;

public class DemoWalletDAO implements WalletDAO {

    private final Map<String, WalletEntity> wallets = new HashMap<>();

    @Override
    public WalletEntity updateWalletBalance(WalletEntity walletEntity, int balance) {
        WalletEntity newWallet = walletEntity.updateBalance(balance);
        wallets.put(walletEntity.username(), newWallet);
        return newWallet;
    }

    @Override
    public int getWalletBalanceByUsername(String username) {
        WalletEntity walletEntity = wallets.get(username);

        if (walletEntity == null) return -1;
        return walletEntity.balance();
    }

    @Override
    public boolean saveWallet(WalletEntity walletEntity) {
        return wallets.putIfAbsent(walletEntity.username(), walletEntity) == null;
    }

    public Map<String, WalletEntity> getWallets() {
        return wallets;
    }
}
