package app.services;

import app.daos.wallet.WalletDAO;
import app.exceptions.ProfileServiceException;
import app.network.Session;
import dtos.GetWalletBalanceRequestDTO;
import dtos.GetWalletBalanceResponseDTO;
import dtos.Response;
import utils.WalletError;

public class WalletService {

    private final WalletDAO walletDAO;

    public WalletService(WalletDAO walletDAO) {
        this.walletDAO = walletDAO;
    }

    public Response getWalletBalance(GetWalletBalanceRequestDTO getWalletBalanceRequestDTO, Session userSession) throws ProfileServiceException {

        int balance = walletDAO.getWalletBalanceByUsername(userSession.getUsername());

        if (balance == -1) {
            throw new ProfileServiceException(WalletError.COULD_NOT_GET_BALANCE);
        }

        return new GetWalletBalanceResponseDTO(getWalletBalanceRequestDTO.requestId(), balance);
    }
}
