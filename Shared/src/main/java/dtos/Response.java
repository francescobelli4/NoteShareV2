package dtos;

public sealed interface Response extends Message permits ErrorResponseDTO, GetWalletBalanceResponseDTO, LoginResponseDTO, LoginUsingTokenResponseDTO, RegisterResponseDTO {
    long requestId();
}
