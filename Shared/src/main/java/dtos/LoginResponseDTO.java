package dtos;

import java.util.Objects;

public record LoginResponseDTO(long requestId, String username) implements Response {

    public LoginResponseDTO {
        Objects.requireNonNull(username);
    }

    @Override
    public String toString() {
        return "LoginResponseDTO[requestId=" + requestId + ", username=" + username + "]";
    }
}
