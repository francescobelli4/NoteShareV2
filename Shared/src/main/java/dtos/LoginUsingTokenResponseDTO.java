package dtos;

import permissions.Role;

import java.util.Objects;

public record LoginUsingTokenResponseDTO(long requestId, String username, Role role, String token) implements Response {

    public LoginUsingTokenResponseDTO {
        Objects.requireNonNull(username);
        Objects.requireNonNull(role);
        Objects.requireNonNull(token);
    }

    @Override
    public String toString() {
        return "LoginResponseDTO[requestId=" + requestId + ", username=" + username + ", role=" + role + "]";
    }
}
