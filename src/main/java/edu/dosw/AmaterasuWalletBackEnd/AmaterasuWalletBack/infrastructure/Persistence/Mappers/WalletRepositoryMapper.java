package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Mappers;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryRequests.WalletDocument;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryResponses.WalletRepositoryResponse;

public class WalletRepositoryMapper {
    public static WalletDocument walletToDocument(Wallet wallet){
        WalletDocument receiptDocument = new WalletDocument();
        receiptDocument.setWalletId(wallet.getWalletId());
        receiptDocument.setClientId(wallet.getClientId());
        receiptDocument.setMoneyAmount(wallet.getMoneyAmount());
        receiptDocument.setUpdatedAt(wallet.getUpdatedAt());
        return receiptDocument;
    }

    public static WalletRepositoryResponse documentToResponse(WalletDocument document){
        return new WalletRepositoryResponse(
                document.getWalletId(),
                document.getClientId(),
                document.getMoneyAmount(),
                document.getUpdatedAt());
    }
}
