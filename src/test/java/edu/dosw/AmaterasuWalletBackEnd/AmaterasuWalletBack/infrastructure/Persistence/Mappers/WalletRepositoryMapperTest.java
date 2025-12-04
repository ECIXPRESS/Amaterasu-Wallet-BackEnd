package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Mappers;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryRequests.WalletDocument;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryResponses.WalletRepositoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletRepositoryMapperTest {

    @Test
    @DisplayName("Should map Wallet to WalletDocument correctly")
    void shouldMapWalletToDocument() {
        
        Wallet wallet = new Wallet();
        wallet.createWallet("CLIENT123", 1000.0);

       
        WalletDocument document = WalletRepositoryMapper.walletToDocument(wallet);

        
        assertNotNull(document);
        assertEquals(wallet.getWalletId(), document.getWalletId());
        assertEquals(wallet.getClientId(), document.getClientId());
        assertEquals(wallet.getMoneyAmount(), document.getMoneyAmount());
        assertEquals(wallet.getUpdatedAt(), document.getUpdatedAt());
    }

    @Test
    @DisplayName("Should map WalletDocument to WalletRepositoryResponse correctly")
    void shouldMapDocumentToResponse() {
    
        WalletDocument document = new WalletDocument();
        document.setWalletId("WAL_12345678");
        document.setClientId("CLIENT123");
        document.setMoneyAmount(1500.0);
        document.setUpdatedAt("2025-01-01T10:00:00.000Z");

   
        WalletRepositoryResponse response = WalletRepositoryMapper.documentToResponse(document);

       
        assertNotNull(response);
        assertEquals("WAL_12345678", response.walletId());
        assertEquals("CLIENT123", response.clientId());
        assertEquals(1500.0, response.moneyAmount());
        assertEquals("2025-01-01T10:00:00.000Z", response.updatedAt());
    }
}
