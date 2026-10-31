package app.daos.user;

import app.entities.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import permissions.Role;

import static org.junit.jupiter.api.Assertions.*;

class DemoUserDAOTest {

    private static DemoUserDAO userDAO;
    private static UserEntity user;

    @BeforeAll
    static void setUp() {
        userDAO = new DemoUserDAO();

        user = new UserEntity("username", "password", Role.STUDENT, "token");
        userDAO.getUsers().put("username", user);
    }

    @Test
    void saveUser() {

        UserEntity newUser = new UserEntity("username1", "password1", Role.STUDENT, "token1");

        assertTrue(userDAO.saveUser(newUser));
        assertEquals(2, userDAO.getUsers().size());

        assertFalse(userDAO.saveUser(newUser));
        assertEquals(2, userDAO.getUsers().size());
    }

    @Test
    void findUserByUsername() {
        UserEntity foundUser = userDAO.findUserByUsername("username");
        assertEquals("username", foundUser.username());
        assertEquals("password", foundUser.hashedPassword());
        assertEquals(Role.STUDENT, foundUser.role());
        assertEquals("token", foundUser.token());

        assertNull(userDAO.findUserByUsername("uusseerrnnaammee"));
    }

    @Test
    void findUserByToken() {

        UserEntity foundUser = userDAO.findUserByToken("token");
        assertEquals("username", foundUser.username());
        assertEquals("password", foundUser.hashedPassword());
        assertEquals(Role.STUDENT, foundUser.role());
        assertEquals("token", foundUser.token());

        assertNull(userDAO.findUserByToken("ttookkeenn"));
    }

    @Test
    void updateUserToken() {

        UserEntity updated = userDAO.updateUserToken(user, "newtoken");

        assertEquals("newtoken", updated.token());
        assertEquals(user.username(), updated.username());
        assertEquals(user.hashedPassword(), updated.hashedPassword());
        assertEquals(user.role(), updated.role());
    }
}