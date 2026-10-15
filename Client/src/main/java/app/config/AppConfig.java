package app.config;

import utils.CLIParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public record AppConfig(Mode mode, GuiType guiType, Locale locale) {

    public AppConfig {
        Objects.requireNonNull(mode);
        Objects.requireNonNull(guiType);
        Objects.requireNonNull(locale);
    }

    private static AppConfig defaults() {
        return new AppConfig(Mode.DEMO, GuiType.FX, Locale.ITALIAN);
    }

    /**
     * This function should return an AppConfig object basing on CLI args.
     * @param args CLI args
     * @return app config
     * @throws IllegalArgumentException in case an option is malformed
     */
    public static AppConfig create(String[] args) throws IllegalArgumentException {

        AppConfig def = defaults();
        HashMap<String, String> options = CLIParser.fromArgs(args);

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

        GuiType parsedGuiType = def.guiType;
        if (options.containsKey("guitype")) {
            String guiType = options.get("guitype");

            try {
                parsedGuiType = GuiType.valueOf(guiType);
            } catch (IllegalArgumentException _) {
                throw new IllegalArgumentException("Invalid value for option: guitype. Use FX or SECONDARY instead.");
            }
        }

        Locale parsedLocale = def.locale;
        if (options.containsKey("locale")) {
            String locale = options.get("locale").toLowerCase();

            boolean isValid = Arrays.stream(Locale.getAvailableLocales()).anyMatch(l -> l.getLanguage().equals(locale));

            if (!isValid) {
                throw new IllegalArgumentException("Invalid value for option: locale. Use it-IT or en-EN instead.");
            }

            parsedLocale = Locale.of(locale);
        }

        return new AppConfig(parsedMode, parsedGuiType, parsedLocale);
    }
}
