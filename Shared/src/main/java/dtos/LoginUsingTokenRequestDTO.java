package dtos;

import java.util.Objects;

public record LoginUsingTokenRequestDTO(long requestId, String token) implements Request {

    public LoginUsingTokenRequestDTO {
        Objects.requireNonNull(token);
    }

    @Override
    public String toString() {
        return "LoginRequestDTO[secret_token]";
    }
}
