package app.network;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import permissions.Permission;
import permissions.Role;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    private static Session session;

    @BeforeAll
    static void beforeAll() {
        session = new Session();
    }

    @Test
    void logIn() {

        assertFalse(session.hasPermission(Permission.BUY_NOTES));

        session.logIn("username", Role.STUDENT);

        assertEquals("username", session.getUsername());
        assertEquals(Role.STUDENT, session.getRole());
        assertTrue(session.isLoggedIn());

        assertTrue(session.hasPermission(Permission.BUY_NOTES));
    }
}