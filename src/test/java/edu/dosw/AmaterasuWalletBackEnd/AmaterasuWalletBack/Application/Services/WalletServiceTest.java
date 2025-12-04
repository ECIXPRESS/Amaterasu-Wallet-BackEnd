package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.Services;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Enums.PaymentStatus;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepositoryProvider;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryResponses.WalletRepositoryResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests.*;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletResponses.*;
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
class WalletServiceTest {

    @Mock
    private WalletRepositoryProvider walletRepositoryProvider;

    @InjectMocks
    private WalletService walletService;

    private WalletRepositoryResponse mockRepositoryResponse;

    @BeforeEach
    void setUp() {
        mockRepositoryResponse = new WalletRepositoryResponse(
            "WAL_12345678",
            "CLIENT123",
            1000.0,
            "2025-01-01T10:00:00.000Z"
        );
    }

    @Test
    @DisplayName("Should create wallet successfully")
    void shouldCreateWalletSuccessfully() {
        // Arrange
        CreateWalletRequest request = new CreateWalletRequest("CLIENT123", 1000.0);
        when(walletRepositoryProvider.save(any())).thenReturn(mockRepositoryResponse);

        // Act
        boolean result = walletService.createWallet(request);

        // Assert
        assertTrue(result);
        verify(walletRepositoryProvider, times(1)).save(any());
    }

    @Test
    @DisplayName("Should return false when creating wallet fails")
    void shouldReturnFalseWhenCreatingWalletFails() {
        // Arrange
        CreateWalletRequest request = new CreateWalletRequest("CLIENT123", 1000.0);
        when(walletRepositoryProvider.save(any())).thenThrow(new RuntimeException("Database error"));

        // Act
        boolean result = walletService.createWallet(request);

        // Assert
        assertFalse(result);
        verify(walletRepositoryProvider, times(1)).save(any());
    }

    @Test
    @DisplayName("Should pay with wallet successfully")
    void shouldPayWithWalletSuccessfully() throws Exception {
        // Arrange
        PayWithWalletRequest request = new PayWithWalletRequest("CLIENT123", 300.0);
        when(walletRepositoryProvider.getByClientId("CLIENT123")).thenReturn(mockRepositoryResponse);
        when(walletRepositoryProvider.save(any())).thenReturn(mockRepositoryResponse);

        // Act
        PayWithWalletResponse response = walletService.payWithWallet(request);

        // Assert
        assertNotNull(response);
        assertEquals(PaymentStatus.COMPLETED, response.paymentStatus());
        verify(walletRepositoryProvider, times(1)).getByClientId("CLIENT123");
        verify(walletRepositoryProvider, times(1)).save(any());
    }

    @Test
    @DisplayName("Should return FAILED when payment fails due to insufficient funds")
    void shouldReturnFailedWhenPaymentFailsDueToInsufficientFunds() throws Exception {
        // Arrange
        PayWithWalletRequest request = new PayWithWalletRequest("CLIENT123", 1500.0);
        when(walletRepositoryProvider.getByClientId("CLIENT123")).thenReturn(mockRepositoryResponse);

        // Act
        PayWithWalletResponse response = walletService.payWithWallet(request);

        // Assert
        assertNotNull(response);
        assertEquals(PaymentStatus.FAILED, response.paymentStatus());
        verify(walletRepositoryProvider, times(1)).getByClientId("CLIENT123");
        verify(walletRepositoryProvider, never()).save(any());
    }

    @Test
    @DisplayName("Should return FAILED when wallet not found")
    void shouldReturnFailedWhenWalletNotFound() throws Exception {
        // Arrange
        PayWithWalletRequest request = new PayWithWalletRequest("CLIENT999", 100.0);
        when(walletRepositoryProvider.getByClientId("CLIENT999"))
            .thenThrow(new Exception("Wallet not found"));

        // Act
        PayWithWalletResponse response = walletService.payWithWallet(request);

        // Assert
        assertNotNull(response);
        assertEquals(PaymentStatus.FAILED, response.paymentStatus());
        verify(walletRepositoryProvider, times(1)).getByClientId("CLIENT999");
    }

    @Test
    @DisplayName("Should add money successfully")
    void shouldAddMoneySuccessfully() throws Exception {
        // Arrange
        AddMoneyRequest request = new AddMoneyRequest("CLIENT123", 500.0);
        when(walletRepositoryProvider.getByClientId("CLIENT123")).thenReturn(mockRepositoryResponse);
        when(walletRepositoryProvider.save(any())).thenReturn(mockRepositoryResponse);

        // Act
        boolean result = walletService.addMoney(request);

        // Assert
        assertTrue(result);
        verify(walletRepositoryProvider, times(1)).getByClientId("CLIENT123");
        verify(walletRepositoryProvider, times(1)).save(any());
    }

    @Test
    @DisplayName("Should return false when adding money fails")
    void shouldReturnFalseWhenAddingMoneyFails() throws Exception {
        // Arrange
        AddMoneyRequest request = new AddMoneyRequest("CLIENT123", 500.0);
        when(walletRepositoryProvider.getByClientId("CLIENT123"))
            .thenThrow(new Exception("Database error"));

        // Act
        boolean result = walletService.addMoney(request);

        // Assert
        assertFalse(result);
        verify(walletRepositoryProvider, times(1)).getByClientId("CLIENT123");
    }

    @Test
    @DisplayName("Should get wallet by client ID successfully")
    void shouldGetWalletByClientIdSuccessfully() throws Exception {
        // Arrange
        GetWalletByClientIdRequest request = new GetWalletByClientIdRequest("CLIENT123");
        when(walletRepositoryProvider.getByClientId("CLIENT123")).thenReturn(mockRepositoryResponse);

        // Act
        GetWalletByClientIdResponse response = walletService.getWalletByClientId(request);

        // Assert
        assertNotNull(response);
        assertEquals("WAL_12345678", response.walletId());
        assertEquals("CLIENT123", response.clientId());
        assertEquals(1000.0, response.moneyAmount());
        assertEquals("2025-01-01T10:00:00.000Z", response.updatedAt());
        verify(walletRepositoryProvider, times(1)).getByClientId("CLIENT123");
    }

    @Test
    @DisplayName("Should return null when wallet not found by client ID")
    void shouldReturnNullWhenWalletNotFoundByClientId() throws Exception {
        // Arrange
        GetWalletByClientIdRequest request = new GetWalletByClientIdRequest("CLIENT999");
        when(walletRepositoryProvider.getByClientId("CLIENT999"))
            .thenThrow(new Exception("Wallet not found"));

        // Act
        GetWalletByClientIdResponse response = walletService.getWalletByClientId(request);

        // Assert
        assertNull(response);
        verify(walletRepositoryProvider, times(1)).getByClientId("CLIENT999");
    }
}
