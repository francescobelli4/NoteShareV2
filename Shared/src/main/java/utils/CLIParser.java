package utils;

import java.util.HashMap;
import java.util.regex.Pattern;

public class CLIParser {

    private static final Pattern OPTION_PATTERN = Pattern.compile("^--([a-zA-Z]+)$");

    /**
     * This function should return an HashMap<OptionName, OptionValue> from String[] args.
     * It should throw an IllegalArgumentException if an arg is malformed.
     * @param args CLI args
     * @return The options hashmap
     */
    public static HashMap<String, String> fromArgs(String[] args) throws IllegalArgumentException {

        if (args.length == 0) return new HashMap<>();

        if (args.length % 2 != 0) throw new IllegalArgumentException("Malformed options: at least one option can't be matched to a value");

        HashMap<String, String> parsedArgs = new HashMap<>();

        for (int i = 0; i < args.length; i++) {

            String option = args[i];
            String value = args[i+1];

            if (OPTION_PATTERN.matcher(option).matches()) {

                if (OPTION_PATTERN.matcher(value).matches()) {
                    throw new IllegalArgumentException(String.format("Malformed value %s for option %s", value, option));
                }

                parsedArgs.put(option.substring(2), value);
                i++;
            } else {
                throw new IllegalArgumentException(String.format("Malformed option: %s", option));
            }
        }

        return parsedArgs;
    }
}
