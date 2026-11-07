package app.appcontrollers;

import app.exceptions.ProfileException;
import app.models.walletmodel.WalletModel;
import app.network.ConnectionManager;
import dtos.ErrorResponseDTO;
import dtos.GetWalletBalanceRequestDTO;
import dtos.GetWalletBalanceResponseDTO;
import utils.ProfileError;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class is an application controller used to satisfy the "manage profile" use case.
 */
public class ProfileController {

    private final WalletModel walletModel;

    public ProfileController(WalletModel walletModel) {
        this.walletModel = walletModel;
    }

    /**
     * This function should set the wallet balance in walletModel using an async request to the server.
     * In case of error (for example, there is no wallet associated with the user), a ProfileException is
     * thrown.
     * @return a CompletableFuture<Void> used by the view to display an error if there's one.
     */
    public CompletableFuture<Void> getBalance() {

        GetWalletBalanceRequestDTO request = new GetWalletBalanceRequestDTO(ConnectionManager.nextRequestId());

        return ConnectionManager.sendAsync(request).orTimeout(10, TimeUnit.SECONDS)
                .thenAccept(response -> {
                   switch (response) {
                       case GetWalletBalanceResponseDTO success -> walletModel.setBalance(success.balance());
                       case ErrorResponseDTO error -> throw new ProfileException((ProfileError) error.error());
                       default -> throw new IllegalStateException("Unexpected response");
                   }
                });
    }
}
