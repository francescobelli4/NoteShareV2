package app.services;

import app.daos.wallet.DemoWalletDAO;
import app.entities.WalletEntity;
import app.exceptions.ProfileServiceException;
import app.network.Session;
import dtos.GetWalletBalanceRequestDTO;
import dtos.GetWalletBalanceResponseDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import permissions.Role;
import utils.WalletServiceError;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceTest {

    private static WalletService walletService;

    @BeforeAll
    static void beforeAll() {
        DemoWalletDAO walletDAO = new DemoWalletDAO();

        walletDAO.saveWallet(new WalletEntity("username", 100));

        walletService = new WalletService(walletDAO);
    }

    @Test
    void getWalletBalance_Success() {

        GetWalletBalanceRequestDTO request = new GetWalletBalanceRequestDTO(1L);
        Session session = new Session();
        session.logIn("username", Role.STUDENT);

        GetWalletBalanceResponseDTO response = (GetWalletBalanceResponseDTO) walletService.getWalletBalance(request, session);

        assertEquals(100, response.balance());
    }

    @Test
    void getWalletBalance_Failure() {

        GetWalletBalanceRequestDTO request = new GetWalletBalanceRequestDTO(1L);
        Session session = new Session();
        session.logIn("pippo", Role.STUDENT);

        ProfileServiceException exception = assertThrows(ProfileServiceException.class, () -> walletService.getWalletBalance(request, session));
        assertEquals(WalletServiceError.COULD_NOT_GET_BALANCE, exception.getProfileError());
    }
}