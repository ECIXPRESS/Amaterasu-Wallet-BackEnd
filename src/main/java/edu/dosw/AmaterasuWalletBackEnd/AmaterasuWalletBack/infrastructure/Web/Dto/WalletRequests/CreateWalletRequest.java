package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests;

public record CreateWalletRequest (
        String clientId,
        double moneyAmount
){
}
