package app.utils;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class TokenManager {

    private static final Logger LOGGER = Logger.getLogger("TokenManager");
    private static final String TOKEN_FILE_NAME = "access_token.txt";

    private TokenManager() { }

    /**
     * This function should save the access token in a file
     * @param token the received access token
     */
    public static void saveToken(String token) {

        try (FileWriter fileWriter = new FileWriter(Utils.getOSLocalPath() + TOKEN_FILE_NAME)) {
            fileWriter.write(token);
        } catch (IOException _) {
            LOGGER.severe("Failed writing access token to file");
        }
    }

    /**
     * This function should erase the saved token
     */
    public static void deleteToken() {

        try (FileWriter fileWriter = new FileWriter(Utils.getOSLocalPath() + TOKEN_FILE_NAME)) {
            fileWriter.write("");
        } catch (IOException _) {
            LOGGER.severe("Failed writing access token to file");
        }
    }

    public static String getToken() throws IOException {
        if (Utils.findFile(Utils.getOSLocalPath() + TOKEN_FILE_NAME) == null) return null;

        return Utils.readFile(Objects.requireNonNull(Utils.findFile(Utils.getOSLocalPath() + TOKEN_FILE_NAME)));
    }
}
