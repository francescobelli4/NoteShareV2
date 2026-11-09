package app.appcontrollers;

import app.exceptions.ProfileException;
import app.models.walletmodel.WalletModel;
import app.network.ConnectionManager;
import dtos.ErrorResponseDTO;
import dtos.GetWalletBalanceRequestDTO;
import dtos.GetWalletBalanceResponseDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import utils.WalletServiceError;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ProfileControllerTest {

    private static ProfileController profileController;
    private static WalletModel walletModel;

    @BeforeAll
    static void beforeAll() {
        walletModel = mock(WalletModel.class);
        profileController = new ProfileController(walletModel);
    }

    @Test
    void getBalance_Success() {

        GetWalletBalanceResponseDTO response = new GetWalletBalanceResponseDTO(1L, 100);

        try (MockedStatic<ConnectionManager> mockedConnectionManger = mockStatic(ConnectionManager.class)) {

            mockedConnectionManger.when(ConnectionManager::nextRequestId).thenReturn(1L);
            mockedConnectionManger.when(() -> ConnectionManager.sendAsync(any(GetWalletBalanceRequestDTO.class))).thenReturn(CompletableFuture.completedFuture(response));

            CompletableFuture<Void> _ = profileController.getBalance();

            verify(walletModel, times(1)).setBalance(100);
        }
    }

    @Test
    void getBalance_Fail() {

        ErrorResponseDTO response = new ErrorResponseDTO(1L, WalletServiceError.COULD_NOT_GET_BALANCE);

        try (MockedStatic<ConnectionManager> mockedConnectionManger = mockStatic(ConnectionManager.class)) {

            mockedConnectionManger.when(ConnectionManager::nextRequestId).thenReturn(1L);
            mockedConnectionManger.when(() -> ConnectionManager.sendAsync(any(GetWalletBalanceRequestDTO.class))).thenReturn(CompletableFuture.completedFuture(response));

            CompletableFuture<Void> resp = profileController.getBalance();

            verify(walletModel, times(0)).setBalance(100);

            resp.exceptionally(ex -> {
                assertInstanceOf(ProfileException.class, ex.getCause());
                return null;
            }).join();
        }
    }
}