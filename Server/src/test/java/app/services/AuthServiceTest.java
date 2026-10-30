package app.services;

import app.daos.user.UserDAO;
import app.entities.UserEntity;
import dtos.RegisterRequestDTO;
import org.junit.jupiter.api.Test;
import permissions.Role;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Test
    void performRegister() {

        UserDAO userDAO = mock(UserDAO.class);
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(1L, "username", "password", Role.STUDENT);

        AuthService authService = new AuthService(userDAO);

        when(userDAO.saveUser(any(UserEntity.class))).thenReturn(true);
    }

    @Test
    void performLogin() {
    }

    @Test
    void performLoginUsingToken() {
    }
}