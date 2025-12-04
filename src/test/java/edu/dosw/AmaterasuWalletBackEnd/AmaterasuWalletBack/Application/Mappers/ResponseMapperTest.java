package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.Mappers;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryResponses.WalletRepositoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseMapperTest {

    @Test
    @DisplayName("Should map WalletRepositoryResponse to Wallet correctly")
    void shouldMapWalletRepositoryResponseToWallet() {
        // Arrange
        WalletRepositoryResponse response = new WalletRepositoryResponse(
            "WAL_12345678",
            "CLIENT123",
            1500.0,
            "2025-01-01T10:00:00.000Z"
        );

        // Act
        Wallet wallet = ResponseMapper.responseToWallet(response);

        // Assert
        assertNotNull(wallet);
        assertEquals("WAL_12345678", wallet.getWalletId());
        assertEquals("CLIENT123", wallet.getClientId());
        assertEquals(1500.0, wallet.getMoneyAmount());
        assertEquals("2025-01-01T10:00:00.000Z", wallet.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle null values in response")
    void shouldHandleNullValuesInResponse() {
        // Arrange
        WalletRepositoryResponse response = new WalletRepositoryResponse(
            null,
            null,
            0.0,
            null
        );

        // Act
        Wallet wallet = ResponseMapper.responseToWallet(response);

        // Assert
        assertNotNull(wallet);
        assertNull(wallet.getWalletId());
        assertNull(wallet.getClientId());
        assertEquals(0.0, wallet.getMoneyAmount());
        assertNull(wallet.getUpdatedAt());
    }
}
