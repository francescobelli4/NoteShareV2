package app.exceptions;

import utils.AuthError;

/**
 * This exception is thrown whenever there is a problem with a login or a register
 */
public class AuthServiceException extends RuntimeException {

    private final transient AuthError authError;

    public AuthServiceException(AuthError authError) {
        this.authError = authError;
        super(authError.toString());
    }

    public AuthError getAuthError() {
        return authError;
    }
}
