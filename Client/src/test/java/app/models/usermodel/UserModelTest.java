package app.models.usermodel;

import org.junit.jupiter.api.Test;
import permissions.Permission;
import permissions.Role;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserModelTest {

    @Test
    void logIn() {
        UserModel userModel = new UserModel();
        UserModelListener listener = mock(UserModelListener.class);

        assertFalse(userModel.hasPermission(Permission.BUY_NOTES));

        userModel.addListener(listener);
        userModel.logIn("username", Role.STUDENT);


        assertEquals("username", userModel.getUsername());
        assertEquals(Role.STUDENT, userModel.getRole());
        assertTrue(userModel.isLoggedIn());
        assertTrue(userModel.hasPermission(Permission.BUY_NOTES));

        verify(listener).onLoggedIn();
    }

    @Test
    void logOut() {

        UserModel userModel = new UserModel();
        UserModelListener listener = mock(UserModelListener.class);

        userModel.addListener(listener);
        userModel.logIn("username", Role.STUDENT);

        userModel.logOut();

        assertNull(userModel.getUsername());
        assertNull(userModel.getRole());
        assertFalse(userModel.isLoggedIn());

        verify(listener).onLoggedOut();
    }
}