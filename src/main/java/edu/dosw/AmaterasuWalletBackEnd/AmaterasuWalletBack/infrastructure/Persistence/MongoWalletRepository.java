package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "spring.data.mongodb.uri", matchIfMissing = true)
public class MongoWalletRepository implements WalletRepository {

    private final Map<String, Wallet> wallets = new HashMap<>();

    @Override
    public Wallet save(Wallet wallet) {
        wallets.put(wallet.getWalletId(), wallet);
        return wallet;
    }

    @Override
    public Optional<Wallet> findByWalletId(String walletId) {
        return Optional.ofNullable(wallets.get(walletId));
    }

    @Override
    public Optional<Wallet> findByClientId(String clientId) {
        return wallets.values().stream()
                .filter(wallet -> wallet.getClientId().equals(clientId))
                .findFirst();
    }

    @Override
    public boolean existsByClientId(String clientId) {
        return wallets.values().stream()
                .anyMatch(wallet -> wallet.getClientId().equals(clientId));
    }

    @Override
    public void delete(String walletId) {
        wallets.remove(walletId);
    }
}