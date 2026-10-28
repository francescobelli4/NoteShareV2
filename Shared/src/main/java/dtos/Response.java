package dtos;

public sealed interface Response extends Message permits LoginResponseDTO, RegisterResponseDTO {
    long requestId();
}
