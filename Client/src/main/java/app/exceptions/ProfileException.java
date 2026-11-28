package app.exceptions;

import errors.ProfileError;

public class ProfileException extends RuntimeException {

    private final transient ProfileError profileError;

    public ProfileException(ProfileError profileError) {
        super(profileError.toString());
        this.profileError = profileError;
    }

    public ProfileError getProfileError() {
        return profileError;
    }
}
