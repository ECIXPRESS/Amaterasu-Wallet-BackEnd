package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MongoWalletRepositoryAdapter implements WalletRepository {

    private final SpringDataMongoWalletRepository mongoRepository;

    public MongoWalletRepositoryAdapter(SpringDataMongoWalletRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Wallet save(Wallet wallet) {
        return mongoRepository.save(wallet);
    }

    @Override
    public Optional<Wallet> findByWalletId(String walletId) {
        return mongoRepository.findById(walletId);
    }

    @Override
    public Optional<Wallet> findByClientId(String clientId) {
        return mongoRepository.findByClientId(clientId);
    }

    @Override
    public boolean existsByClientId(String clientId) {
        return mongoRepository.existsByClientId(clientId);
    }

    @Override
    public void delete(String walletId) {
        mongoRepository.deleteById(walletId);
    }
}