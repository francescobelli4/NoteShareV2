package dtos;

public sealed interface Request extends Message permits LoginRequestDTO, RegisterRequestDTO, LoginUsingTokenRequestDTO {
    long requestId();
}
