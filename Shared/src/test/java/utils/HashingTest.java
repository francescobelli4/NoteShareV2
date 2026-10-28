package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashingTest {

    @Test
    void hashString() {
        String password = "ExtrEm3LYS3cR3tp455W0rd";
        String same = "ExtrEm3LYS3cR3tp455W0rd";
        String hashed = Hashing.hashString(password);

        // It should create different hashes for two equal strings
        assertNotEquals(hashed, Hashing.hashString(same));

        assertTrue(Hashing.verifyHash(password, hashed));
    }
}