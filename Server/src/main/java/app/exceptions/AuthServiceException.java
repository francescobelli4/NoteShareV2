package app.exceptions;

public class AuthServiceException extends RuntimeException {
    public AuthServiceException(String message) {
        super(message);
    }
}
