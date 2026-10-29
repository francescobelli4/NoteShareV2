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

    /**
     * The only way to update a parameter in a record, is to re-create the UserEntity.
     * @param newToken the new token
     * @return the "updated" instance that will replace the old one
     */
    public UserEntity updateToken(String newToken) {
        return new UserEntity(username, hashedPassword, role, newToken);
    }
}
