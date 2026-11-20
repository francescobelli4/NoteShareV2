package app.utils;

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
}