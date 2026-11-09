package app.daos.wallet;

import app.entities.WalletEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemoWalletDAOTest {

    private static DemoWalletDAO demoWalletDAO;
    private static WalletEntity testWallet;

    @BeforeAll
    static void beforeAll() {
        demoWalletDAO = new DemoWalletDAO();
        testWallet = new WalletEntity(
                "username",
                100
        );
        demoWalletDAO.saveWallet(testWallet);
    }

    @Test
    void updateWalletBalance() {
        testWallet = demoWalletDAO.updateWalletBalance(testWallet, 50);
        assertEquals(50, testWallet.balance());
        assertEquals(50, demoWalletDAO.getWalletBalanceByUsername(testWallet.username()));
    }

    @Test
    void getWalletBalanceByUsername_Fail() {
        assertEquals(-1, demoWalletDAO.getWalletBalanceByUsername("pippo"));
    }

    @Test
    void saveWallet() {
        WalletEntity newWallet = new WalletEntity("pluto", 10);

        assertFalse(demoWalletDAO.saveWallet(testWallet));
        assertTrue(demoWalletDAO.saveWallet(newWallet));
        assertTrue(demoWalletDAO.getWallets().containsKey(newWallet.username()));
    }
}