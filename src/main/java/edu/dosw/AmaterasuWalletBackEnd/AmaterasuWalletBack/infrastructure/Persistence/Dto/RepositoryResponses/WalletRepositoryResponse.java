package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryResponses;

public record WalletRepositoryResponse(
        String walletId,
        String clientId,
        double moneyAmount,
        String updatedAt
) {
}
