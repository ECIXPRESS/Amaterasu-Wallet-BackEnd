package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MongoWalletRepository implements WalletRepository {

    private final MongoTemplate mongoTemplate;

    public MongoWalletRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Wallet save(Wallet wallet) {
        return mongoTemplate.save(wallet);
    }

    @Override
    public Optional<Wallet> findByWalletId(String walletId) {
        Query query = new Query(Criteria.where("walletId").is(walletId));
        Wallet wallet = mongoTemplate.findOne(query, Wallet.class);
        return Optional.ofNullable(wallet);
    }

    @Override
    public Optional<Wallet> findByClientId(String clientId) {
        Query query = new Query(Criteria.where("clientId").is(clientId));
        Wallet wallet = mongoTemplate.findOne(query, Wallet.class);
        return Optional.ofNullable(wallet);
    }

    @Override
    public boolean existsByClientId(String clientId) {
        Query query = new Query(Criteria.where("clientId").is(clientId));
        return mongoTemplate.exists(query, Wallet.class);
    }

    @Override
    public void delete(String walletId) {
        Query query = new Query(Criteria.where("walletId").is(walletId));
        mongoTemplate.remove(query, Wallet.class);
    }
}
