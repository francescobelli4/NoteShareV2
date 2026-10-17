package utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CLIParserTest {

    @Test
    void fromArgsSuccessful() {
        String[] args = {"--mode", "DEMO", "--guitype", "FX", "--locale", "it"};

        Map<String, String> options = CLIParser.fromArgs(args);

        assertAll("Options",
                () -> assertEquals("mode", options.keySet().toArray()[0]),
                () -> assertEquals("DEMO", options.values().toArray()[0]),
                () -> assertEquals("guitype", options.keySet().toArray()[1]),
                () -> assertEquals("FX", options.values().toArray()[1]),
                () -> assertEquals("locale", options.keySet().toArray()[2]),
                () -> assertEquals("it", options.values().toArray()[2])
        );
    }

    @Test
    void fromArgsMalformedOptions() {
        String[] args = {"--mode", "DEMO", "--guitype", "FX", "--locale"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CLIParser.fromArgs(args));

        assertEquals("Malformed options: at least one option can't be matched to a value", exception.getMessage());
    }

    @Test
    void fromArgsMalformedOption() {
        String[] args = {"-mode", "DEMO", "--guitype", "FX", "--locale", "it"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CLIParser.fromArgs(args));

        assertEquals("Malformed option: -mode", exception.getMessage());
    }

    @Test
    void fromArgsMalformedOptionValue() {
        String[] args = {"--mode", "--DEMO", "--guitype", "FX", "--locale", "it"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CLIParser.fromArgs(args));

        assertEquals("Malformed value --DEMO for option --mode", exception.getMessage());
    }
}