package dtos;

import permissions.Role;

import java.util.Objects;

public record LoginResponseDTO(long requestId, String username, Role role) implements Response {

    public LoginResponseDTO {
        Objects.requireNonNull(username);
        Objects.requireNonNull(role);
    }

    @Override
    public String toString() {
        return "LoginResponseDTO[requestId=" + requestId + ", username=" + username + ", role=" + role + "]";
    }
}
