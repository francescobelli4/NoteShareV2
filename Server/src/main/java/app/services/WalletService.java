package app.services;

import app.daos.wallet.WalletDAO;
import app.exceptions.ProfileServiceException;
import app.network.Session;
import dtos.GetWalletBalanceRequestDTO;
import dtos.GetWalletBalanceResponseDTO;
import dtos.Response;
import utils.WalletServiceError;

public class WalletService {

    private final WalletDAO walletDAO;

    public WalletService(WalletDAO walletDAO) {
        this.walletDAO = walletDAO;
    }

    /**
     * This function should find the wallet for the user specified in the Session and generate a response with its
     * balance.
     * @param getWalletBalanceRequestDTO the request sent from the client
     * @param userSession the session for that client
     * @return a Response with the balance
     * @throws ProfileServiceException in case that wallet is not found
     */
    public Response getWalletBalance(GetWalletBalanceRequestDTO getWalletBalanceRequestDTO, Session userSession) throws ProfileServiceException {

        if (!userSession.isLoggedIn()) throw new ProfileServiceException(WalletServiceError.NOT_LOGGED_IN);

        int balance = walletDAO.getWalletBalanceByUsername(userSession.getUsername());

        if (balance == -1) {
            throw new ProfileServiceException(WalletServiceError.COULD_NOT_GET_BALANCE);
        }

        return new GetWalletBalanceResponseDTO(getWalletBalanceRequestDTO.requestId(), balance);
    }
}
