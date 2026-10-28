package app.appcontrollers;

import app.beans.LoginBean;
import app.beans.RegisterBean;
import app.exceptions.AuthException;
import app.models.usermodel.UserModel;
import app.network.ConnectionManager;
import dtos.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import permissions.Role;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Test
    void performLogin_Success() {

        UserModel userModel = mock(UserModel.class);
        AuthController authController = new AuthController(userModel);
        LoginBean loginBean = new LoginBean("username", "password");
        LoginResponseDTO response = new LoginResponseDTO(123L, "username", Role.STUDENT);

        // Mocking the connection manager and crafting a mocked response
        try (MockedStatic<ConnectionManager> mockedCM = mockStatic(ConnectionManager.class)) {

            // Returning the right requestId
            mockedCM.when(ConnectionManager::nextRequestId).thenReturn(123L);

            // We want ConnectionManager.sendAsync to return a "fake" future that is already completed. In this way,
            // authController.performLogin receives this future as response.
            mockedCM.when(() -> ConnectionManager.sendAsync(any(LoginRequestDTO.class))).thenReturn(CompletableFuture.completedFuture(response));

            CompletableFuture<Void> _ = authController.performLogin(loginBean);

            verify(userModel, times(1)).logIn("username", Role.STUDENT);
        }
    }

    @Test
    void performLogin_LoginFailed() {

        UserModel userModel = mock(UserModel.class);
        AuthController authController = new AuthController(userModel);
        LoginBean loginBean = new LoginBean("username", "password");
        ErrorResponseDTO response = new ErrorResponseDTO(123L, "login failed");

        // Mocking the connection manager and crafting a mocked response
        try (MockedStatic<ConnectionManager> mockedCM = mockStatic(ConnectionManager.class)) {

            // Returning the right requestId
            mockedCM.when(ConnectionManager::nextRequestId).thenReturn(123L);

            // We want ConnectionManager.sendAsync to return a "fake" future that is already completed. In this way,
            // authController.performLogin receives this future as response.
            mockedCM.when(() -> ConnectionManager.sendAsync(any(LoginRequestDTO.class))).thenReturn(CompletableFuture.completedFuture(response));

            CompletableFuture<Void> resp = authController.performLogin(loginBean);

            verify(userModel, times(0)).logIn("username", Role.STUDENT);

            resp.exceptionally(ex -> {
                assertInstanceOf(AuthException.class, ex.getCause());
                return null;
            }).join();
        }
    }

    @Test
    void performRegister_Success() {
        UserModel userModel = mock(UserModel.class);
        AuthController authController = new AuthController(userModel);
        RegisterBean registerBean = new RegisterBean("username", "password", Role.STUDENT);
        RegisterResponseDTO response = new RegisterResponseDTO(123L, "username", Role.STUDENT);

        // Mocking the connection manager and crafting a mocked response
        try (MockedStatic<ConnectionManager> mockedCM = mockStatic(ConnectionManager.class)) {

            // Returning the right requestId
            mockedCM.when(ConnectionManager::nextRequestId).thenReturn(123L);

            // We want ConnectionManager.sendAsync to return a "fake" future that is already completed. In this way,
            // authController.performRegister receives this future as response.
            mockedCM.when(() -> ConnectionManager.sendAsync(any(RegisterRequestDTO.class))).thenReturn(CompletableFuture.completedFuture(response));

            CompletableFuture<Void> _ = authController.performRegister(registerBean);

            verify(userModel, times(1)).logIn("username", Role.STUDENT);
        }
    }

    @Test
    void performRegister_RegisterFailed() {

        UserModel userModel = mock(UserModel.class);
        AuthController authController = new AuthController(userModel);
        RegisterBean registerBean = new RegisterBean("username", "password", Role.STUDENT);
        ErrorResponseDTO response = new ErrorResponseDTO(123L, "register failed");

        // Mocking the connection manager and crafting a mocked response
        try (MockedStatic<ConnectionManager> mockedCM = mockStatic(ConnectionManager.class)) {

            // Returning the right requestId
            mockedCM.when(ConnectionManager::nextRequestId).thenReturn(123L);

            // We want ConnectionManager.sendAsync to return a "fake" future that is already completed. In this way,
            // authController.performLogin receives this future as response.
            mockedCM.when(() -> ConnectionManager.sendAsync(any(RegisterRequestDTO.class))).thenReturn(CompletableFuture.completedFuture(response));

            CompletableFuture<Void> resp = authController.performRegister(registerBean);

            verify(userModel, times(0)).logIn("username", Role.STUDENT);

            resp.exceptionally(ex -> {
                assertInstanceOf(AuthException.class, ex.getCause());
                return null;
            }).join();
        }
    }
}