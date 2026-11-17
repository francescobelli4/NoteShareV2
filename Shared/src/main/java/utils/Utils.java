package utils;

public class Utils {

    private Utils() {

    }

    public static boolean isAlphanumeric(String str) {

        for (int i = 0; i < str.length(); i++) {

            char c = str.charAt(i);

            if (!Character.isLetterOrDigit(c)) return false;
        }

        return true;
    }
}
