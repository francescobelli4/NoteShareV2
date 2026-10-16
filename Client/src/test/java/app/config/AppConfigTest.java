package app.config;

import org.junit.jupiter.api.Test;
import utils.CLIParser;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    @Test
    void createWithCorrectParams() {

        String[] args = {"--mode", "DEMO", "--guitype", "FX", "--locale", "it"};

        AppConfig appConfig = AppConfig.create(args);

        assertAll("Options",
                () -> assertEquals(Mode.DEMO, appConfig.mode()),
                () -> assertEquals(GuiType.FX, appConfig.guiType()),
                () -> assertEquals(Locale.ITALIAN, appConfig.locale())
        );
    }

    @Test
    void createWithEmptyParams() {
        String[] args = {};

        AppConfig appConfig = AppConfig.create(args);

        assertAll("Default Options",
                () -> assertEquals(Mode.DEMO, appConfig.mode()),
                () -> assertEquals(GuiType.FX, appConfig.guiType()),
                () -> assertEquals(Locale.ITALIAN, appConfig.locale())
        );
    }

    @Test
    void createThrowsExceptionOnInvalidMode() {
        String[] args = {"--mode", "EARLY_ACCESS"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            AppConfig.create(args);
        });

        assertEquals("Invalid value for option: mode. Use DEMO or FULL instead.", exception.getMessage());
    }

    @Test
    void createThrowsExceptionOnInvalidGuiType() {
        String[] args = {"--guitype", "WEB"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            AppConfig.create(args);
        });

        assertEquals("Invalid value for option: guitype. Use FX or SECONDARY instead.", exception.getMessage());
    }

    @Test
    void createThrowsExceptionOnInvalidLocale() {
        String[] args = {"--locale", "unitorvergata"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            AppConfig.create(args);
        });

        assertTrue(exception.getMessage().contains("Invalid value for option: locale"));
    }
}