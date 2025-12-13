package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryResponses.WalletRepositoryResponse;

public interface WalletRepositoryProvider {
    WalletRepositoryResponse save(Wallet wallet);
    WalletRepositoryResponse getByClientId(String clientId) throws Exception;
    void delete(String clientId) throws Exception;
}
