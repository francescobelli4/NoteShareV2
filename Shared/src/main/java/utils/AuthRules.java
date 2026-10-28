package utils;

import java.util.Optional;

public class AuthRules {

    public static final int USERNAME_MIN = 4;
    public static final int USERNAME_MAX = 10;
    public static final int PASSWORD_MIN = 8;
    public static final int PASSWORD_MAX = 20;

    private AuthRules() {
        // Nothing to do :D
    }

    public static Optional<UsernameError> isUsernameValid(String username) {

        int length = username.length();

        if (length == 0) return Optional.of(UsernameError.NULL_OR_EMPTY);

        if (length < USERNAME_MIN) return Optional.of(UsernameError.TOO_SHORT);
        if (length > USERNAME_MAX) return Optional.of(UsernameError.TOO_LONG);

        if (!isAlphanumeric(username)) return Optional.of(UsernameError.NOT_ALPHANUMERIC);

        return Optional.empty();
    }

    public static Optional<PasswordError> isPasswordValid(String password) {

        int length = password.length();

        if (length == 0) return Optional.of(PasswordError.NULL_OR_EMPTY);

        if (length < PASSWORD_MIN) return Optional.of(PasswordError.TOO_SHORT);
        if (length > PASSWORD_MAX) return Optional.of(PasswordError.TOO_LONG);

        if (!isAlphanumeric(password)) return Optional.of(PasswordError.NOT_ALPHANUMERIC);

        return Optional.empty();
    }

    private static boolean isAlphanumeric(String str) {

        for (int i = 0; i < str.length(); i++) {

            char c = str.charAt(i);

            if (!Character.isLetterOrDigit(c)) return false;
        }

        return true;
    }
}
