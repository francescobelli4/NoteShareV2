package dtos;

import java.util.Objects;

public record LoginRequestDTO(long requestId, String username, String password) implements Request {

    public LoginRequestDTO {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
    }

    @Override
    public String toString() {
        return "LoginRequestDTO[requestId=" + requestId + ", username=" + username + ", password=" + password + "]";
    }
}
