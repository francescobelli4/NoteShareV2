package app.exceptions;

import utils.AuthError;
import utils.ProfileError;

/**
 * This exception is thrown whenever there is a problem with a login or a register
 */
public class ProfileServiceException extends RuntimeException {

    private final transient ProfileError profileError;

    public ProfileServiceException(ProfileError profileError) {
        this.profileError = profileError;
        super(profileError.toString());
    }

    public ProfileError getProfileError() {
        return profileError;
    }
}
