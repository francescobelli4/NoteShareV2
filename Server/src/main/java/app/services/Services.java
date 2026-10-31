package app.services;

import java.util.Objects;

public record Services(AuthService authService) {

    public Services {
        Objects.requireNonNull(authService);
    }
}
