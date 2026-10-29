package app.entities;

import permissions.Role;

import java.util.Objects;

public record UserEntity(String username, String hashedPassword, Role role, String token) {

    public UserEntity {
        Objects.requireNonNull(username);
        Objects.requireNonNull(hashedPassword);
        Objects.requireNonNull(role);
        Objects.requireNonNull(token);
    }
}
