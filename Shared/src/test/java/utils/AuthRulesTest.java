package utils;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthRulesTest {

    @Test
    void isUsernameValid_Success() {

        String username = "username";

        Optional<UsernameError> error = AuthRules.isUsernameValid(username);

        assertFalse(error.isPresent());
    }

    @Test
    void isUsernameValid_Empty() {

        String username = "";

        Optional<UsernameError> error = AuthRules.isUsernameValid(username);

        assertTrue(error.isPresent());
        assertEquals(UsernameError.NULL_OR_EMPTY, error.get());
    }

    @Test
    void isUsernameValid_TooShort() {

        String username = "u";

        Optional<UsernameError> error = AuthRules.isUsernameValid(username);

        assertTrue(error.isPresent());
        assertEquals(UsernameError.TOO_SHORT, error.get());
    }

    @Test
    void isUsernameValid_TooLong() {

        String username = "usernameeeeeeeeeeeeeeeeeeeeeee";

        Optional<UsernameError> error = AuthRules.isUsernameValid(username);

        assertTrue(error.isPresent());
        assertEquals(UsernameError.TOO_LONG, error.get());
    }

    @Test
    void isUsernameValid_NotAlphanumeric() {

        String username = "us3-.rname";

        Optional<UsernameError> error = AuthRules.isUsernameValid(username);

        assertTrue(error.isPresent());
        assertEquals(UsernameError.NOT_ALPHANUMERIC, error.get());
    }

    @Test
    void isPasswordValid_Success() {

        String username = "username";

        Optional<PasswordError> error = AuthRules.isPasswordValid(username);

        assertFalse(error.isPresent());
    }

    @Test
    void isPasswordValid_Empty() {

        String username = "";

        Optional<PasswordError> error = AuthRules.isPasswordValid(username);

        assertTrue(error.isPresent());
        assertEquals(PasswordError.NULL_OR_EMPTY, error.get());
    }

    @Test
    void isPasswordValid_TooShort() {

        String username = "u";

        Optional<PasswordError> error = AuthRules.isPasswordValid(username);

        assertTrue(error.isPresent());
        assertEquals(PasswordError.TOO_SHORT, error.get());
    }

    @Test
    void isPasswordValid_TooLong() {

        String username = "usernameeeeeeeeeeeeeeeeeeeeeee";

        Optional<PasswordError> error = AuthRules.isPasswordValid(username);

        assertTrue(error.isPresent());
        assertEquals(PasswordError.TOO_LONG, error.get());
    }

    @Test
    void isPasswordValid_NotAlphanumeric() {

        String username = "us3-.rname";

        Optional<PasswordError> error = AuthRules.isPasswordValid(username);

        assertTrue(error.isPresent());
        assertEquals(PasswordError.NOT_ALPHANUMERIC, error.get());
    }
}