package dtos;

import permissions.Role;

import java.util.Objects;

public record RegisterRequestDTO(long requestId, String username, String password, Role role) implements Request {

    public RegisterRequestDTO {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        Objects.requireNonNull(role);
    }

    @Override
    public String toString() {
        return "RegisterRequestDTO[requestId=" + requestId + ", username=" + username + ", role=" + role + "]";
    }
}
