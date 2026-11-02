package utils;

public enum PasswordError implements AuthError {
    NULL_OR_EMPTY,
    TOO_SHORT,
    TOO_LONG,
    NOT_ALPHANUMERIC
}