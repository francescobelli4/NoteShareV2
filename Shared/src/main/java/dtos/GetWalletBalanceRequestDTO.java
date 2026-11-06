package dtos;

public record GetWalletBalanceRequestDTO(long requestId) implements Request {

    @Override
    public String toString() {
        return "GetWalletRequestDTO[requestId=" + requestId + "]";
    }
}
