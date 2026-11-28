package app.utils;

import app.exceptions.LibraryException;
import errors.LibraryError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LocalesTest {

    @BeforeAll
    static void beforeAll() {
        Locales.setCurrentLocale(Locale.ENGLISH);
    }

    @Test
    void getBundle() {
        assertNotNull(Locales.getBundle());
        assertEquals(Locale.ENGLISH, Locales.getBundle().getLocale());
    }

    @Test
    void get() {
        assertEquals("Explore", Locales.get("explore"));
        Locales.setCurrentLocale(Locale.ITALIAN);
        assertEquals("Esplora", Locales.get("explore"));
        Locales.setCurrentLocale(Locale.ENGLISH);
    }

    @Test
    void testGet() {
        assertEquals("Note testNote was created", Locales.get("dialog.note_created", "testNote"));
    }

    @Test
    void getErrorLabelTest() {

        Locales.setCurrentLocale(Locale.ENGLISH);

        LibraryException libraryException = new LibraryException(LibraryError.NOTE_ALREADY_EXISTS, "testNote");
        String errorLabel = Locales.getErrorLabel(libraryException.getLibraryError(), libraryException.getCausingItemName());

        assertEquals("Note testNote already exists", errorLabel);
    }

    @Test
    void getErrorLabelTest_Unexpected() {

        Locales.setCurrentLocale(Locale.ENGLISH);
        String errorLabel = Locales.getErrorLabel(null);

        assertEquals("Unexpected error", errorLabel);
    }
}