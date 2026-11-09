package app.services;

import java.util.Objects;

public record Services(AuthService authService, WalletService walletService) {

    public Services {
        Objects.requireNonNull(authService);
        Objects.requireNonNull(walletService);
    }
}
