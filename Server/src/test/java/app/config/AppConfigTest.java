package app.config;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    @Test
    void createWithCorrectParams() {

        String[] args = {"--mode", "DEMO"};

        AppConfig appConfig = AppConfig.create(args);

        assertAll("Options",
                () -> assertEquals(Mode.DEMO, appConfig.mode())
        );
    }

    @Test
    void createWithEmptyParams() {
        String[] args = {};

        AppConfig appConfig = AppConfig.create(args);

        assertAll("Default Options",
                () -> assertEquals(Mode.DEMO, appConfig.mode())
        );
    }

    @Test
    void createThrowsExceptionOnInvalidMode() {
        String[] args = {"--mode", "EARLY_ACCESS"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> AppConfig.create(args));

        assertEquals("Invalid value for option: mode. Use DEMO or FULL instead.", exception.getMessage());
    }

}