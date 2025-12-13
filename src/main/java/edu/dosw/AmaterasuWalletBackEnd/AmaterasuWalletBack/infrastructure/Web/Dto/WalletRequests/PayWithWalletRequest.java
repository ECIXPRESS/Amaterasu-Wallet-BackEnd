package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests;

public record PayWithWalletRequest(
        String clientId,
        double moneyAmount
){
}
