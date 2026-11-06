package dtos;

public sealed interface Request extends Message permits LoginRequestDTO, RegisterRequestDTO, LoginUsingTokenRequestDTO, GetWalletBalanceRequestDTO {
    long requestId();
}
