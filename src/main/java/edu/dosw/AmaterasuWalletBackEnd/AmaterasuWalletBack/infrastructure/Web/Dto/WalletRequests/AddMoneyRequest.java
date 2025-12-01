package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests;

public record AddMoneyRequest(
        String clientId,
        double moneyAmount
) {
}
