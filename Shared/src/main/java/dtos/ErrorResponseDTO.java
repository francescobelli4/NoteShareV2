package dtos;

import utils.ErrorCode;

import java.util.Objects;

/**
 * This response should notify an error to the user. ErrorCode is an interface that needs to be specified in the
 * right error type.
 * @param requestId
 * @param error
 */
public record ErrorResponseDTO(long requestId, ErrorCode error) implements Response {

    public ErrorResponseDTO {
        Objects.requireNonNull(error);
    }

    @Override
    public String toString() {
        return "ErrorResponseDTO[requestId=" + requestId + ", error=" + error + "]";
    }
}
