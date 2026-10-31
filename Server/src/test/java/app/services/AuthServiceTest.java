package app.services;

import app.daos.user.UserDAO;
import app.entities.UserEntity;
import app.exceptions.AuthServiceException;
import dtos.RegisterRequestDTO;
import dtos.RegisterResponseDTO;
import dtos.Response;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import permissions.Role;
import utils.AuthServiceError;
import utils.PasswordError;
import utils.UsernameError;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Test
    void performRegister() {

        UserDAO userDAO = mock(UserDAO.class);
        RegisterRequestDTO request = new RegisterRequestDTO(1L, "username", "password", Role.STUDENT);

        AuthService authService = new AuthService(userDAO);

        when(userDAO.saveUser(any(UserEntity.class))).thenReturn(true);

        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");

        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {

            mockedUUID.when(UUID::randomUUID).thenReturn(uuid);
            Response response = authService.performRegister(request);

            assertEquals(uuid.toString(), ((RegisterResponseDTO) response).token());
            assertEquals(1L, response.requestId());
            assertEquals("username", ((RegisterResponseDTO) response).username());
            assertEquals(Role.STUDENT, ((RegisterResponseDTO) response).role());
        }
    }

    @Test
    void performRegister_InvalidUsername() {

        UserDAO userDAO = mock(UserDAO.class);
        RegisterRequestDTO request = new RegisterRequestDTO(1L, "u", "password", Role.STUDENT);

        AuthService authService = new AuthService(userDAO);

        when(userDAO.saveUser(any(UserEntity.class))).thenReturn(true);

        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> authService.performRegister(request));

        assertEquals(UsernameError.TOO_SHORT, exception.getAuthError());
    }

    @Test
    void performRegister_InvalidPassword() {

        UserDAO userDAO = mock(UserDAO.class);
        RegisterRequestDTO request = new RegisterRequestDTO(1L, "username", "p", Role.STUDENT);

        AuthService authService = new AuthService(userDAO);

        when(userDAO.saveUser(any(UserEntity.class))).thenReturn(true);

        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> authService.performRegister(request));

        assertEquals(PasswordError.TOO_SHORT, exception.getAuthError());
    }

    @Test
    void performRegister_InvalidRole() {

        UserDAO userDAO = mock(UserDAO.class);
        RegisterRequestDTO request = new RegisterRequestDTO(1L, "username", "password", Role.MODERATOR);

        AuthService authService = new AuthService(userDAO);

        when(userDAO.saveUser(any(UserEntity.class))).thenReturn(true);

        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> authService.performRegister(request));

        assertEquals(AuthServiceError.INVALID_ROLE, exception.getAuthError());
    }

    @Test
    void performRegister_UsernameTaken() {

        UserDAO userDAO = mock(UserDAO.class);
        RegisterRequestDTO request = new RegisterRequestDTO(1L, "username", "password", Role.STUDENT);

        AuthService authService = new AuthService(userDAO);

        when(userDAO.saveUser(any(UserEntity.class))).thenReturn(false);

        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> authService.performRegister(request));

        assertEquals(AuthServiceError.USERNAME_TAKEN, exception.getAuthError());
    }

    @Test
    void performLogin() {
    }

    @Test
    void performLoginUsingToken() {
    }
}