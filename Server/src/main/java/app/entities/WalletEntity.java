package app.entities;

import java.util.Objects;

public record WalletEntity(String username, int balance) {

    public WalletEntity {
        Objects.requireNonNull(username);
    }

    public WalletEntity updateBalance(int coins) {
        return new WalletEntity(username, coins);
    }
}
