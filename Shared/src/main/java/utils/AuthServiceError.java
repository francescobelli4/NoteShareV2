package utils;

public enum AuthServiceError implements AuthError {
    USERNAME_TAKEN,
    INVALID_ROLE,
    USER_NOT_FOUND,
    WRONG_PASSWORD,
    PASSWORDS_DO_NOT_MATCH,
}
