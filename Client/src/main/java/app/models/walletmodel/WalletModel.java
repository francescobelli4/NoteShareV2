package app.models.walletmodel;

import app.utils.ListenerHelper;
import app.utils.Subscription;

/**
 * This model represents the user's wallet in the app. It should only be used for students.
 */
public class WalletModel {

    private final ListenerHelper<WalletModelListener> listeners = new ListenerHelper<>();
    private int balance = 0;

    public Subscription addListener(WalletModelListener listener) {
        return listeners.subscribe(listener);
    }

    public void setBalance(int coins) {
        balance = coins;
        listeners.triggerEvent(WalletModelListener::onBalanceUpdated);
    }

    public int getBalance() {
        return balance;
    }
}
