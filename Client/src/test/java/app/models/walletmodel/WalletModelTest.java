package app.models.walletmodel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletModelTest {

    private static WalletModel testWallet;

    @BeforeAll
    static void beforeAll() {
        testWallet = new WalletModel();
    }

    @Test
    void setBalance() {
        testWallet.setBalance(100);
        assertEquals(100, testWallet.getBalance());
    }
}