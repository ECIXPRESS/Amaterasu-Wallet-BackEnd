package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.Mappers;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryResponses.WalletRepositoryResponse;

public class ResponseMapper {

    public static Wallet responseToWallet(WalletRepositoryResponse response){
        Wallet wallet = new Wallet();
        wallet.setWalletId(response.walletId());
        wallet.setClientId(response.clientId());
        wallet.setMoneyAmount(response.moneyAmount());
        wallet.setUpdatedAt(response.updatedAt());
        return wallet;
    }
}
