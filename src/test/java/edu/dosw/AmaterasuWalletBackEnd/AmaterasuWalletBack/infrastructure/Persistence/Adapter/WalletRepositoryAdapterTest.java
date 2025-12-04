package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Adapter;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryRequests.WalletDocument;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryResponses.WalletRepositoryResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Repositories.MongoWalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletRepositoryAdapterTest {

    @Mock
    private MongoWalletRepository mongoRepository;

    @InjectMocks
    private WalletRepositoryAdapter walletRepositoryAdapter;

    private Wallet wallet;
    private WalletDocument walletDocument;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        wallet.createWallet("CLIENT123", 1000.0);

        walletDocument = new WalletDocument();
        walletDocument.setWalletId("WAL_12345678");
        walletDocument.setClientId("CLIENT123");
        walletDocument.setMoneyAmount(1000.0);
        walletDocument.setUpdatedAt("2025-01-01T10:00:00.000Z");
    }

    @Test
    @DisplayName("Should save wallet successfully")
    void shouldSaveWalletSuccessfully() {
        // Arrange
        when(mongoRepository.save(any(WalletDocument.class))).thenReturn(walletDocument);

        // Act
        WalletRepositoryResponse response = walletRepositoryAdapter.save(wallet);

        // Assert
        assertNotNull(response);
        assertEquals("CLIENT123", response.clientId());
        assertEquals(1000.0, response.moneyAmount());
        verify(mongoRepository, times(1)).save(any(WalletDocument.class));
    }

    @Test
    @DisplayName("Should get wallet by client ID successfully")
    void shouldGetWalletByClientIdSuccessfully() throws Exception {
        // Arrange
        when(mongoRepository.findByClientId("CLIENT123")).thenReturn(walletDocument);

        // Act
        WalletRepositoryResponse response = walletRepositoryAdapter.getByClientId("CLIENT123");

        // Assert
        assertNotNull(response);
        assertEquals("WAL_12345678", response.walletId());
        assertEquals("CLIENT123", response.clientId());
        assertEquals(1000.0, response.moneyAmount());
        verify(mongoRepository, times(1)).findByClientId("CLIENT123");
    }

    @Test
    @DisplayName("Should throw exception when wallet not found by client ID")
    void shouldThrowExceptionWhenWalletNotFoundByClientId() {
        // Arrange
        when(mongoRepository.findByClientId("CLIENT999")).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            walletRepositoryAdapter.getByClientId("CLIENT999");
        });

        assertTrue(exception.getMessage().contains("Wallet of CLIENT999 does not exist"));
        verify(mongoRepository, times(1)).findByClientId("CLIENT999");
    }

    @Test
    @DisplayName("Should delete wallet successfully")
    void shouldDeleteWalletSuccessfully() throws Exception {
        // Arrange
        when(mongoRepository.findByClientId("CLIENT123")).thenReturn(walletDocument);
        doNothing().when(mongoRepository).delete(any(WalletDocument.class));

        // Act
        walletRepositoryAdapter.delete("CLIENT123");

        // Assert
        verify(mongoRepository, times(1)).findByClientId("CLIENT123");
        verify(mongoRepository, times(1)).delete(walletDocument);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent wallet")
    void shouldThrowExceptionWhenDeletingNonExistentWallet() {
        // Arrange
        when(mongoRepository.findByClientId("CLIENT999")).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            walletRepositoryAdapter.delete("CLIENT999");
        });

        assertTrue(exception.getMessage().contains("Wallet of CLIENT999 does not exist"));
        verify(mongoRepository, times(1)).findByClientId("CLIENT999");
        verify(mongoRepository, never()).delete(any());
    }
}
