package utils;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LibraryRulesTest {

    @Test
    void isNameValid_Success() {

        String name = "name";

        Optional<LibraryError> error = LibraryRules.isNameValid(name);

        assertFalse(error.isPresent());
    }

    @Test
    void isUsernameValid_Empty() {

        String username = "";

        Optional<LibraryError> error = LibraryRules.isNameValid(username);

        assertTrue(error.isPresent());
        assertEquals(LibraryError.NAME_EMPTY, error.get());
    }

    @Test
    void isUsernameValid_TooLong() {

        String username = "nameeeeeeeeeeeeeeeeeeeeeee";

        Optional<LibraryError> error = LibraryRules.isNameValid(username);

        assertTrue(error.isPresent());
        assertEquals(LibraryError.NAME_TOO_LONG, error.get());
    }
}