package app.config;

import utils.CLIParser;

import java.util.Map;
import java.util.Objects;

public record AppConfig(Mode mode) {

    public AppConfig {
        Objects.requireNonNull(mode);
    }

    private static AppConfig defaults() {
        return new AppConfig(Mode.DEMO);
    }

    /**
     * This function should return an AppConfig object basing on CLI args.
     * @param args CLI args
     * @return app config
     * @throws IllegalArgumentException in case an option is malformed
     */
    public static AppConfig create(String[] args) throws IllegalArgumentException {

        AppConfig def = defaults();
        Map<String, String> options = CLIParser.fromArgs(args);

        if (options.isEmpty()) return def;

        Mode parsedMode = def.mode;
        if (options.containsKey("mode")) {
            String mode = options.get("mode");

            try {
                parsedMode = Mode.valueOf(mode);
            } catch (IllegalArgumentException _) {
                throw new IllegalArgumentException("Invalid value for option: mode. Use DEMO or FULL instead.");
            }
        }

        return new AppConfig(parsedMode);
    }
}
