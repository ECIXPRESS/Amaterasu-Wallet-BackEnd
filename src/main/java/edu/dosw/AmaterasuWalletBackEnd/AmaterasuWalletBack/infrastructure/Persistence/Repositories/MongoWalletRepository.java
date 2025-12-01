package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Repositories;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryRequests.WalletDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MongoWalletRepository extends MongoRepository<WalletDocument, String> {
    @Query("{ 'clientId': ?0 }")
    WalletDocument findByClientId(String clientId);
}
