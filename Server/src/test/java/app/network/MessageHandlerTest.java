package app.network;

import app.exceptions.AuthServiceException;
import app.services.AuthService;
import app.services.Services;
import app.services.WalletService;
import dtos.*;
import org.junit.jupiter.api.Test;
import permissions.Role;
import utils.AuthServiceError;
import utils.PasswordError;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageHandlerTest {

    @Test
    void handleMessage_WrongMessage() {

        AuthService mockedAuthService = mock(AuthService.class);
        WalletService mockedWalletService = mock(WalletService.class);
        Services services = new Services(mockedAuthService, mockedWalletService);
        Session session = new Session();

        MessageHandler messageHandler = new MessageHandler(services);
        ErrorResponseDTO loginRequestDTO = new ErrorResponseDTO(1L, PasswordError.TOO_SHORT);

        Response resp = messageHandler.handleMessage(loginRequestDTO, session);
        assertNull(resp);
    }

    @Test
    void handleMessage_LoginReq() {

        AuthService mockedAuthService = mock(AuthService.class);
        WalletService mockedWalletService = mock(WalletService.class);
        Services services = new Services(mockedAuthService, mockedWalletService);
        Session session = new Session();

        MessageHandler messageHandler = new MessageHandler(services);
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(1L, "username", "password");
        when(mockedAuthService.performLogin(loginRequestDTO, session)).thenReturn(new LoginResponseDTO(
                1L,
                "username",
                Role.STUDENT,
                "token"
        ));

        Response resp = messageHandler.handleMessage(loginRequestDTO, session);

        verify(mockedAuthService).performLogin(loginRequestDTO, session);
        assertInstanceOf(LoginResponseDTO.class, resp);
    }

    @Test
    void handleMessage_LoginReqError() {

        AuthService mockedAuthService = mock(AuthService.class);
        WalletService mockedWalletService = mock(WalletService.class);
        Services services = new Services(mockedAuthService, mockedWalletService);
        Session session = new Session();

        MessageHandler messageHandler = new MessageHandler(services);
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(1L, "username", "password");
        when(mockedAuthService.performLogin(loginRequestDTO, session)).thenThrow(new AuthServiceException(
                PasswordError.TOO_SHORT
        ));

        Response resp = messageHandler.handleMessage(loginRequestDTO, session);

        verify(mockedAuthService).performLogin(loginRequestDTO, session);
        assertInstanceOf(ErrorResponseDTO.class, resp);
    }

    @Test
    void handleMessage_RegisterReq() {

        AuthService mockedAuthService = mock(AuthService.class);
        WalletService mockedWalletService = mock(WalletService.class);
        Services services = new Services(mockedAuthService, mockedWalletService);
        Session session = new Session();

        MessageHandler messageHandler = new MessageHandler(services);
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(1L, "username", "password", Role.STUDENT);
        when(mockedAuthService.performRegister(registerRequestDTO, session)).thenReturn(new RegisterResponseDTO(
                1L,
                "username",
                Role.STUDENT,
                "token"
        ));

        Response resp = messageHandler.handleMessage(registerRequestDTO, session);

        verify(mockedAuthService).performRegister(registerRequestDTO, session);
        assertInstanceOf(RegisterResponseDTO.class, resp);
    }

    @Test
    void handleMessage_RegisterReqError() {

        AuthService mockedAuthService = mock(AuthService.class);
        WalletService mockedWalletService = mock(WalletService.class);
        Services services = new Services(mockedAuthService, mockedWalletService);
        Session session = new Session();

        MessageHandler messageHandler = new MessageHandler(services);
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(1L, "username", "password", Role.STUDENT);
        when(mockedAuthService.performRegister(registerRequestDTO, session)).thenThrow(new AuthServiceException(
                AuthServiceError.WRONG_PASSWORD
        ));

        Response resp = messageHandler.handleMessage(registerRequestDTO, session);

        verify(mockedAuthService).performRegister(registerRequestDTO, session);
        assertInstanceOf(ErrorResponseDTO.class, resp);
    }

    @Test
    void handleMessage_LoginUsingTokenReq() {

        AuthService mockedAuthService = mock(AuthService.class);
        WalletService mockedWalletService = mock(WalletService.class);
        Services services = new Services(mockedAuthService, mockedWalletService);
        Session session = new Session();

        MessageHandler messageHandler = new MessageHandler(services);
        LoginUsingTokenRequestDTO loginUsingTokenRequestDTO = new LoginUsingTokenRequestDTO(1L, "token");
        when(mockedAuthService.performLoginUsingToken(loginUsingTokenRequestDTO, session)).thenReturn(new LoginUsingTokenResponseDTO(
                1L,
                "username",
                Role.STUDENT,
                "token"
        ));

        Response resp = messageHandler.handleMessage(loginUsingTokenRequestDTO, session);

        verify(mockedAuthService).performLoginUsingToken(loginUsingTokenRequestDTO, session);
        assertInstanceOf(LoginUsingTokenResponseDTO.class, resp);
    }
}