package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletResponses;

public record GetWalletByClientIdResponse(
        String walletId,
        String clientId,
        double moneyAmount,
        String updatedAt
) {
}
