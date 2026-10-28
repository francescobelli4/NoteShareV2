package dtos;

import permissions.Role;

import java.util.Objects;

public record RegisterResponseDTO(long requestId, String username, Role role) implements Response {

    public RegisterResponseDTO {
        Objects.requireNonNull(username);
        Objects.requireNonNull(role);
    }

    @Override
    public String toString() {
        return "RegisterResponseDTO[requestId=" + requestId + ", username=" + username + ", role=" + role + "]";
    }
}
