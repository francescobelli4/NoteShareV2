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

public class ProfileController {

    private final WalletModel walletModel;

    public ProfileController(WalletModel walletModel) {
        this.walletModel = walletModel;
    }

    public CompletableFuture<Void> getBalance() {

        GetWalletBalanceRequestDTO request = new GetWalletBalanceRequestDTO(ConnectionManager.nextRequestId());

        return ConnectionManager.sendAsync(request).orTimeout(10, TimeUnit.SECONDS)
                .thenAccept(response -> {
                   switch (response) {
                       case GetWalletBalanceResponseDTO success -> walletModel.setBalance(success.balance());
                       case ErrorResponseDTO error -> throw new ProfileException((ProfileError) error.error());
                       default                          -> throw new IllegalStateException("Unexpected response");
                   }
                });
    }
}
