package app.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

    private Utils() { }

    /**
     * This function returns a path to the app's local folder
     * @return the path to the app's local folder
     */
    public static String getOSLocalPath() {
        String path;
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            path = System.getenv("APPDATA") + "\\NoteShare\\";
        } else {
            path = System.getProperty("user.home") + "/.local/share/NoteShare/";
        }

        return path;
    }

    /**
     * This function should find a file from its path
     * @param path the file's path
     * @return the file or null if it's not found
     */
    public static File findFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }

    /**
     * This function should read a file content
     * @param file the file that has to be red
     * @return the content of the file
     * @throws IOException failed to read the file (does not exist?)
     */
    public static String readFile(File file) throws IOException {
        return Files.readString(Path.of(file.getAbsolutePath()));
    }

    /**
     * This function should find a folder from its path
     * @param path the folder's path
     * @return the folder or null if it's not found
     */
    public static File findFolder(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            return file;
        }
        return null;
    }

    /**
     * This function should create a new directory
     * @param path the new directory's path
     */
    public static boolean createDir(String path) {
        File dir = new File(path);

        if (dir.exists()) {
            return false;
        }

        return dir.mkdirs();
    }
}
