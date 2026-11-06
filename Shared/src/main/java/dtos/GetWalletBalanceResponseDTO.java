package dtos;

public record GetWalletBalanceResponseDTO(long requestId, int balance) implements Response {

    @Override
    public String toString() {
        return "GetWalletBalanceResponseDTO[requestId=" + requestId + ", username=" + balance + "]";
    }
}
