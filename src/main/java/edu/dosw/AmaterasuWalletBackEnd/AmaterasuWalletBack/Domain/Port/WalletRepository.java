package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;

import java.util.Optional;

public interface WalletRepository {
    Wallet save(Wallet wallet);
    Optional<Wallet> findByWalletId(String walletId);
    Optional<Wallet> findByClientId(String clientId);
    boolean existsByClientId(String clientId);
    void delete(String walletId);
}
