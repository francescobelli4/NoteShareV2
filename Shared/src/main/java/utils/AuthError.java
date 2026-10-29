package utils;

public sealed interface AuthError extends ErrorCode permits PasswordError, UsernameError, AuthServiceError { }
