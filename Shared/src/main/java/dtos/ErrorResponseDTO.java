package dtos;

import java.util.Objects;

public record ErrorResponseDTO(long requestId, String error) implements Response {

    public ErrorResponseDTO {
        Objects.requireNonNull(error);
    }

    @Override
    public String toString() {
        return "ErrorResponseDTO[requestId=" + requestId + ", error=" + error + "]";
    }
}
