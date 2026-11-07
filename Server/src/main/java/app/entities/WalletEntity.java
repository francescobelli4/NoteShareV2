package app.entities;

import java.util.Objects;

/**
 * This class represents a Wallet as an entity. Every wallet is associated to a username and has a balance prop.
 * @param username
 * @param balance
 */
public record WalletEntity(String username, int balance) {

    public WalletEntity {
        Objects.requireNonNull(username);
    }

    /**
     * This function should update the wallet's balance. Just like happens for every record class, it can't be updated,
     * so I have to create a new WalletEntity that replaces the old one
     * @param coins the new balance
     * @return a new WalletEntity
     */
    public WalletEntity updateBalance(int coins) {
        return new WalletEntity(username, coins);
    }
}
