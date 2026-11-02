package app.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void findFile() throws IOException {
        Path tempFile = Files.writeString(tempDir.resolve("a.txt"), "test");
        assertEquals(tempFile.toFile(), Utils.findFile(tempFile.toString()));
    }

    @Test
    void findFile_NotExists() {
        assertNull(Utils.findFile(tempDir.resolve("abc.txt").toString()));
    }

    @Test
    void readFile() throws IOException {
        Path tempFile = Files.writeString(tempDir.resolve("a.txt"), "test");
        assertEquals("test", Utils.readFile(tempFile.toFile()));
    }

    @Test
    void findFolder() {
        assertEquals(tempDir.toFile(), Utils.findFolder(tempDir.toString()));
    }

    @Test
    void findFolder_NotExists() {
        assertNull(Utils.findFolder(tempDir.resolve("abc.txt").toString()));
    }

    @Test
    void createDir() {
        Path folder = tempDir.resolve("nested");

        assertTrue(Utils.createDir(folder.toString()));
        assertTrue(Files.isDirectory(folder));
    }

    @Test
    void createDir_AlreadyExists() {
        assertFalse(Utils.createDir(tempDir.toString()));
    }
}