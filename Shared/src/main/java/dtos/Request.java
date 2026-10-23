package dtos;

public sealed interface Request extends Message permits LoginRequestDTO {
    long requestId();
}
