package app.services;

import app.daos.user.UserDAO;
import app.entities.UserEntity;
import app.exceptions.AuthServiceException;
import dtos.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import permissions.Role;
import utils.AuthServiceError;
import utils.Hashing;
import utils.PasswordError;
import utils.UsernameError;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

        UserDAO userDAO = mock(UserDAO.class);
        AuthService authService = new AuthService(userDAO);
        LoginRequestDTO request = new LoginRequestDTO(1L, "username", "password");

        UUID oldToken = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID newToken = UUID.fromString("00000000-0000-0000-0000-000000000002");

        UserEntity oldUser = new UserEntity("username", "hash", Role.STUDENT, oldToken.toString());
        UserEntity updatedUser = new UserEntity("username", "hash", Role.STUDENT, newToken.toString());

        when(userDAO.findUserByUsername("username")).thenReturn(oldUser);
        when(userDAO.updateUserToken(oldUser, newToken.toString())).thenReturn(updatedUser);

        try (
                MockedStatic<Hashing> mockedHashing = Mockito.mockStatic(Hashing.class);
                MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)
        ) {

            mockedHashing.when(() -> Hashing.verifyHash(anyString(), anyString())).thenReturn(true);
            mockedUUID.when(UUID::randomUUID).thenReturn(newToken);

            Response response = authService.performLogin(request);

            LoginResponseDTO login = assertInstanceOf(LoginResponseDTO.class, response);
            assertEquals(1L, login.requestId());
            assertEquals("username", login.username());
            assertEquals(Role.STUDENT, login.role());
            assertEquals(newToken.toString(), login.token());

            verify(userDAO).updateUserToken(oldUser, newToken.toString());
        }
    }

    @Test
    void performLogin_UserNotFound() {

        UserDAO userDAO = mock(UserDAO.class);
        AuthService authService = new AuthService(userDAO);
        LoginRequestDTO request = new LoginRequestDTO(1L, "username", "password");

        when(userDAO.findUserByUsername("username")).thenReturn(null);

        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> authService.performLogin(request));

        assertEquals(AuthServiceError.USER_NOT_FOUND, exception.getAuthError());
    }

    @Test
    void performLogin_WrongPassword() {

        UserDAO userDAO = mock(UserDAO.class);
        AuthService authService = new AuthService(userDAO);
        LoginRequestDTO request = new LoginRequestDTO(1L, "username", "password");

        UUID oldToken = UUID.fromString("00000000-0000-0000-0000-000000000001");

        UserEntity oldUser = new UserEntity("username", "hash", Role.STUDENT, oldToken.toString());

        when(userDAO.findUserByUsername("username")).thenReturn(oldUser);

        try (
                MockedStatic<Hashing> mockedHashing = Mockito.mockStatic(Hashing.class)
        ) {

            mockedHashing.when(() -> Hashing.verifyHash(anyString(), anyString())).thenReturn(false);

            AuthServiceException exception = assertThrows(AuthServiceException.class, () -> authService.performLogin(request));

            assertEquals(AuthServiceError.WRONG_PASSWORD, exception.getAuthError());
        }
    }

    @Test
    void performLoginUsingToken() {

        UserDAO userDAO = mock(UserDAO.class);
        AuthService authService = new AuthService(userDAO);
        LoginUsingTokenRequestDTO request = new LoginUsingTokenRequestDTO(1L, "token");

        UUID oldToken = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID newToken = UUID.fromString("00000000-0000-0000-0000-000000000002");

        UserEntity oldUser = new UserEntity("username", "hash", Role.STUDENT, oldToken.toString());
        UserEntity updatedUser = new UserEntity("username", "hash", Role.STUDENT, newToken.toString());

        when(userDAO.findUserByToken("token")).thenReturn(oldUser);
        when(userDAO.updateUserToken(oldUser, newToken.toString())).thenReturn(updatedUser);

        try (
                MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)
        ) {
            mockedUUID.when(UUID::randomUUID).thenReturn(newToken);

            Response response = authService.performLoginUsingToken(request);

            LoginUsingTokenResponseDTO login = assertInstanceOf(LoginUsingTokenResponseDTO.class, response);
            assertEquals(1L, login.requestId());
            assertEquals("username", login.username());
            assertEquals(Role.STUDENT, login.role());
            assertEquals(newToken.toString(), login.token());

            verify(userDAO).updateUserToken(oldUser, newToken.toString());
        }
    }

    @Test
    void performLoginUsingToken_NoUser() {

        UserDAO userDAO = mock(UserDAO.class);
        AuthService authService = new AuthService(userDAO);
        LoginUsingTokenRequestDTO request = new LoginUsingTokenRequestDTO(1L, "token");

        when(userDAO.findUserByToken("token")).thenReturn(null);

        assertNull(authService.performLoginUsingToken(request));
    }
}