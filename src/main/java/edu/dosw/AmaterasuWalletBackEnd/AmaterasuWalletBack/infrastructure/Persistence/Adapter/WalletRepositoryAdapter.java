package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Adapter;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepositoryProvider;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryRequests.WalletDocument;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryResponses.WalletRepositoryResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Mappers.WalletRepositoryMapper;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Repositories.MongoWalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WalletRepositoryAdapter implements WalletRepositoryProvider {

    private final MongoWalletRepository mongoRepository;

    @Override
    public WalletRepositoryResponse save(Wallet wallet) {
        WalletDocument document = WalletRepositoryMapper.walletToDocument(wallet);
        mongoRepository.save(document);
        return WalletRepositoryMapper.documentToResponse(document);
    }

    @Override
    public WalletRepositoryResponse getByClientId(String clientId) throws Exception {
        WalletDocument document = mongoRepository.findByClientId(clientId);
        if(document == null){
            log.error("Wallet of {} does not exist", clientId);
            throw new Exception("Wallet of "+clientId+" does not exist");
        }
        return WalletRepositoryMapper.documentToResponse(document);
    }

    @Override
    public void delete(String clientId) throws Exception{
        WalletDocument document = mongoRepository.findByClientId(clientId);
        if(document == null){
            log.error("Wallet of {} does not exist", clientId);
            throw new Exception("Wallet of "+clientId+" does not exist");
        }
        mongoRepository.delete(document);
    }
}