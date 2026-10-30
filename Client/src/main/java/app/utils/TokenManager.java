package app.utils;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class TokenManager {

    private final static Logger LOGGER = Logger.getLogger("TokenManager");

    private TokenManager() { }

    /**
     * This function should save the access token in a file
     * @param token the received access token
     */
    public static void saveToken(String token) {

        try (FileWriter fileWriter = new FileWriter(Utils.getOSLocalPath() + "access_token.txt")) {
            fileWriter.write(token);
        } catch (IOException _) {
            LOGGER.severe("Failed writing access token to file");
        }
    }

    public static String getToken() throws IOException {
        if (Utils.findFile(Utils.getOSLocalPath() + "access_token.txt") == null) return null;

        return Utils.readFile(Objects.requireNonNull(Utils.findFile(Utils.getOSLocalPath() + "access_token.txt")));
    }
}
