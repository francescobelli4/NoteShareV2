package app.utils;

import errors.ErrorCode;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <a href="https://docs.oracle.com/javase/tutorial/i18n/resbundle/propfile.html">For more info</a>
 *
 */
public class Locales {

    private static final String BASE = "locales.locale";
    private static Locale currentLocale = Locale.ENGLISH;
    private static ResourceBundle bundle = ResourceBundle.getBundle(BASE, currentLocale);

    private Locales() {}

    public static void setCurrentLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle(BASE, currentLocale);
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static String get(String key, Object... args) {
        return MessageFormat.format(bundle.getString(key), args);
    }

    public static String getErrorLabel(ErrorCode errorCode, Object... args) {
        Enum<?> errorCodeAsEnum = (Enum<?>) errorCode;
        String key = errorCodeAsEnum.getDeclaringClass().getSimpleName() + "." + errorCodeAsEnum.name();

        try {
            return get(key, args);
        } catch (MissingResourceException _) {
            return "Unexpected error";
        }
    }
}
