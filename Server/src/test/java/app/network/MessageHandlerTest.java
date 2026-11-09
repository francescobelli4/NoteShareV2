package app.network;

import app.exceptions.AuthServiceException;
import app.services.AuthService;
import app.services.Services;
import app.services.WalletService;
import dtos.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import permissions.Role;
import utils.AuthServiceError;
import utils.PasswordError;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageHandlerTest {

    private static AuthService authService;
    private static WalletService walletService;
    private static Session session;
    private static MessageHandler messageHandler;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        walletService = mock(WalletService.class);
        Services services = new Services(authService, walletService);
        session = new Session();

        messageHandler = new MessageHandler(services);
    }

    @Test
    void handleMessage_WrongMessage() {
        ErrorResponseDTO loginRequestDTO = new ErrorResponseDTO(1L, PasswordError.TOO_SHORT);

        Response resp = messageHandler.handleMessage(loginRequestDTO, session);
        assertNull(resp);
    }

    @Test
    void handleMessage_LoginReq() {

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(1L, "username", "password");
        when(authService.performLogin(loginRequestDTO, session)).thenReturn(new LoginResponseDTO(
                1L,
                "username",
                Role.STUDENT,
                "token"
        ));

        Response resp = messageHandler.handleMessage(loginRequestDTO, session);

        verify(authService).performLogin(loginRequestDTO, session);
        assertInstanceOf(LoginResponseDTO.class, resp);
    }

    @Test
    void handleMessage_LoginReqError() {

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(1L, "username", "password");
        when(authService.performLogin(loginRequestDTO, session)).thenThrow(new AuthServiceException(
                PasswordError.TOO_SHORT
        ));

        Response resp = messageHandler.handleMessage(loginRequestDTO, session);

        verify(authService).performLogin(loginRequestDTO, session);
        assertInstanceOf(ErrorResponseDTO.class, resp);
    }

    @Test
    void handleMessage_RegisterReq() {

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(1L, "username", "password", Role.STUDENT);
        when(authService.performRegister(registerRequestDTO, session)).thenReturn(new RegisterResponseDTO(
                1L,
                "username",
                Role.STUDENT,
                "token"
        ));

        Response resp = messageHandler.handleMessage(registerRequestDTO, session);

        verify(authService).performRegister(registerRequestDTO, session);
        assertInstanceOf(RegisterResponseDTO.class, resp);
    }

    @Test
    void handleMessage_RegisterReqError() {

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(1L, "username", "password", Role.STUDENT);
        when(authService.performRegister(registerRequestDTO, session)).thenThrow(new AuthServiceException(
                AuthServiceError.WRONG_PASSWORD
        ));

        Response resp = messageHandler.handleMessage(registerRequestDTO, session);

        verify(authService).performRegister(registerRequestDTO, session);
        assertInstanceOf(ErrorResponseDTO.class, resp);
    }

    @Test
    void handleMessage_LoginUsingTokenReq() {

        LoginUsingTokenRequestDTO loginUsingTokenRequestDTO = new LoginUsingTokenRequestDTO(1L, "token");
        when(authService.performLoginUsingToken(loginUsingTokenRequestDTO, session)).thenReturn(new LoginUsingTokenResponseDTO(
                1L,
                "username",
                Role.STUDENT,
                "token"
        ));

        Response resp = messageHandler.handleMessage(loginUsingTokenRequestDTO, session);

        verify(authService).performLoginUsingToken(loginUsingTokenRequestDTO, session);
        assertInstanceOf(LoginUsingTokenResponseDTO.class, resp);
    }

    @Test
    void handleMessage_GetWalletBalance() {

        GetWalletBalanceRequestDTO getWalletBalanceRequestDTO = new GetWalletBalanceRequestDTO(1L);

        when(walletService.getWalletBalance(getWalletBalanceRequestDTO, session)).thenReturn(new GetWalletBalanceResponseDTO(1L, 100));

        Response resp = messageHandler.handleMessage(getWalletBalanceRequestDTO, session);

        verify(walletService).getWalletBalance(getWalletBalanceRequestDTO, session);
        assertInstanceOf(GetWalletBalanceResponseDTO.class, resp);
    }
}