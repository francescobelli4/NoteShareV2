package app.exceptions;

import utils.AuthError;

public class AuthException extends RuntimeException {

    private final transient AuthError authError;

    public AuthException(AuthError authError) {
        super(authError.toString());
        this.authError = authError;
    }

    public AuthError getAuthError() {
        return authError;
    }
}
