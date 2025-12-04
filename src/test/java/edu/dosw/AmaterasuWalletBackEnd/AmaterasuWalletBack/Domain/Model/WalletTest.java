package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
    }

    @Test
    @DisplayName("Should create wallet with valid data")
    void shouldCreateWalletWithValidData() {
        // Arrange
        String clientId = "CLIENT123";
        double initialAmount = 1000.0;

        // Act
        Wallet createdWallet = wallet.createWallet(clientId, initialAmount);

        // Assert
        assertNotNull(createdWallet);
        assertNotNull(createdWallet.getWalletId());
        assertTrue(createdWallet.getWalletId().startsWith("WAL_"));
        assertEquals(clientId, createdWallet.getClientId());
        assertEquals(initialAmount, createdWallet.getMoneyAmount());
        assertNotNull(createdWallet.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw exception when clientId is null")
    void shouldThrowExceptionWhenClientIdIsNull() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> wallet.createWallet(null, 1000.0)
        );
        assertEquals("ClientId es requerido", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when clientId is empty")
    void shouldThrowExceptionWhenClientIdIsEmpty() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> wallet.createWallet("", 1000.0)
        );
        assertEquals("ClientId es requerido", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when clientId is blank")
    void shouldThrowExceptionWhenClientIdIsBlank() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> wallet.createWallet("   ", 1000.0)
        );
        assertEquals("ClientId es requerido", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when initial amount is negative")
    void shouldThrowExceptionWhenInitialAmountIsNegative() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> wallet.createWallet("CLIENT123", -100.0)
        );
        assertEquals("El monto no puede ser negativo", exception.getMessage());
    }

    @Test
    @DisplayName("Should create wallet with zero initial amount")
    void shouldCreateWalletWithZeroInitialAmount() {
        // Arrange
        String clientId = "CLIENT123";
        double initialAmount = 0.0;

        // Act
        Wallet createdWallet = wallet.createWallet(clientId, initialAmount);

        // Assert
        assertNotNull(createdWallet);
        assertEquals(0.0, createdWallet.getMoneyAmount());
    }

    @Test
    @DisplayName("Should add money successfully")
    void shouldAddMoneySuccessfully() {
        // Arrange
        wallet.createWallet("CLIENT123", 1000.0);
        double amountToAdd = 500.0;

        // Act
        wallet.addMoney(amountToAdd);

        // Assert
        assertEquals(1500.0, wallet.getMoneyAmount());
    }

    @Test
    @DisplayName("Should throw exception when adding zero or negative amount")
    void shouldThrowExceptionWhenAddingZeroOrNegativeAmount() {
        // Arrange
        wallet.createWallet("CLIENT123", 1000.0);

        // Act & Assert
        IllegalArgumentException exception1 = assertThrows(
            IllegalArgumentException.class,
            () -> wallet.addMoney(0.0)
        );
        assertEquals("El monto a agregar debe ser positivo", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(
            IllegalArgumentException.class,
            () -> wallet.addMoney(-100.0)
        );
        assertEquals("El monto a agregar debe ser positivo", exception2.getMessage());
    }

    @Test
    @DisplayName("Should withdraw money successfully")
    void shouldWithdrawMoneySuccessfully() {
        // Arrange
        wallet.createWallet("CLIENT123", 1000.0);
        double amountToWithdraw = 300.0;

        // Act
        wallet.withdrawMoney(amountToWithdraw);

        // Assert
        assertEquals(700.0, wallet.getMoneyAmount());
    }

    @Test
    @DisplayName("Should throw exception when withdrawing more than available")
    void shouldThrowExceptionWhenWithdrawingMoreThanAvailable() {
        // Arrange
        wallet.createWallet("CLIENT123", 500.0);

        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> wallet.withdrawMoney(600.0)
        );
        assertEquals("Fondos insuficientes en la billetera", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when withdrawing zero or negative amount")
    void shouldThrowExceptionWhenWithdrawingZeroOrNegativeAmount() {
        // Arrange
        wallet.createWallet("CLIENT123", 1000.0);

        // Act & Assert
        IllegalArgumentException exception1 = assertThrows(
            IllegalArgumentException.class,
            () -> wallet.withdrawMoney(0.0)
        );
        assertEquals("El monto a retirar debe ser positivo", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(
            IllegalArgumentException.class,
            () -> wallet.withdrawMoney(-100.0)
        );
        assertEquals("El monto a retirar debe ser positivo", exception2.getMessage());
    }

    @Test
    @DisplayName("Should update timestamp when adding money")
    void shouldUpdateTimestampWhenAddingMoney() throws InterruptedException {
        // Arrange
        wallet.createWallet("CLIENT123", 1000.0);
        String initialTimestamp = wallet.getUpdatedAt();
        Thread.sleep(100);

        // Act
        wallet.addMoney(100.0);

        // Assert
        assertNotEquals(initialTimestamp, wallet.getUpdatedAt());
    }

    @Test
    @DisplayName("Should update timestamp when withdrawing money")
    void shouldUpdateTimestampWhenWithdrawingMoney() throws InterruptedException {
        // Arrange
        wallet.createWallet("CLIENT123", 1000.0);
        String initialTimestamp = wallet.getUpdatedAt();
        Thread.sleep(100);

        // Act
        wallet.withdrawMoney(100.0);

        // Assert
        assertNotEquals(initialTimestamp, wallet.getUpdatedAt());
    }

    @Test
    @DisplayName("Should test Wallet setters and getters")
    void shouldTestWalletSettersAndGetters() {
        // Arrange
        Wallet testWallet = new Wallet();
        String walletId = "WAL_TEST123";
        String clientId = "CLIENT456";
        double moneyAmount = 2000.0;
        String updatedAt = "2025-01-01T10:00:00.000Z";

        // Act
        testWallet.setWalletId(walletId);
        testWallet.setClientId(clientId);
        testWallet.setMoneyAmount(moneyAmount);
        testWallet.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(walletId, testWallet.getWalletId());
        assertEquals(clientId, testWallet.getClientId());
        assertEquals(moneyAmount, testWallet.getMoneyAmount());
        assertEquals(updatedAt, testWallet.getUpdatedAt());
    }

    @Test
    @DisplayName("Should test AllArgsConstructor")
    void shouldTestAllArgsConstructor() {
        // Arrange & Act
        Wallet testWallet = new Wallet(
            "WAL_TEST",
            "CLIENT123",
            1500.0,
            "2025-01-01T10:00:00.000Z"
        );

        // Assert
        assertEquals("WAL_TEST", testWallet.getWalletId());
        assertEquals("CLIENT123", testWallet.getClientId());
        assertEquals(1500.0, testWallet.getMoneyAmount());
        assertEquals("2025-01-01T10:00:00.000Z", testWallet.getUpdatedAt());
    }

    @Test
    @DisplayName("Should test NoArgsConstructor")
    void shouldTestNoArgsConstructor() {
        // Act
        Wallet testWallet = new Wallet();

        // Assert
        assertNotNull(testWallet);
        assertNull(testWallet.getWalletId());
        assertNull(testWallet.getClientId());
        assertEquals(0.0, testWallet.getMoneyAmount());
        assertNull(testWallet.getUpdatedAt());
    }

    @Test
    @DisplayName("Should test equals method - same object")
    void shouldTestEqualsMethodSameObject() {
        // Arrange
        Wallet wallet1 = new Wallet("WAL_123", "CLIENT123", 1000.0, "2025-01-01T10:00:00.000Z");

        // Act & Assert
        assertEquals(wallet1, wallet1);
    }

    @Test
    @DisplayName("Should test equals method - equal objects")
    void shouldTestEqualsMethodEqualObjects() {
        // Arrange
        Wallet wallet1 = new Wallet("WAL_123", "CLIENT123", 1000.0, "2025-01-01T10:00:00.000Z");
        Wallet wallet2 = new Wallet("WAL_123", "CLIENT123", 1000.0, "2025-01-01T10:00:00.000Z");

        // Act & Assert
        assertEquals(wallet1, wallet2);
        assertEquals(wallet2, wallet1);
    }

    @Test
    @DisplayName("Should test equals method - different objects")
    void shouldTestEqualsMethodDifferentObjects() {
        // Arrange
        Wallet wallet1 = new Wallet("WAL_123", "CLIENT123", 1000.0, "2025-01-01T10:00:00.000Z");
        Wallet wallet2 = new Wallet("WAL_456", "CLIENT456", 2000.0, "2025-01-02T10:00:00.000Z");

        // Act & Assert
        assertNotEquals(wallet1, wallet2);
    }

    @Test
    @DisplayName("Should test equals method - null comparison")
    void shouldTestEqualsMethodNullComparison() {
        // Arrange
        Wallet wallet1 = new Wallet("WAL_123", "CLIENT123", 1000.0, "2025-01-01T10:00:00.000Z");

        // Act & Assert
        assertNotEquals(wallet1, null);
    }

    @Test
    @DisplayName("Should test equals method - different class")
    void shouldTestEqualsMethodDifferentClass() {
        // Arrange
        Wallet wallet1 = new Wallet("WAL_123", "CLIENT123", 1000.0, "2025-01-01T10:00:00.000Z");
        String notAWallet = "Not a wallet";

        // Act & Assert
        assertNotEquals(wallet1, notAWallet);
    }

    @Test
    @DisplayName("Should test hashCode method - equal objects have same hashCode")
    void shouldTestHashCodeMethodEqualObjects() {
        // Arrange
        Wallet wallet1 = new Wallet("WAL_123", "CLIENT123", 1000.0, "2025-01-01T10:00:00.000Z");
        Wallet wallet2 = new Wallet("WAL_123", "CLIENT123", 1000.0, "2025-01-01T10:00:00.000Z");

        // Act & Assert
        assertEquals(wallet1.hashCode(), wallet2.hashCode());
    }

    @Test
    @DisplayName("Should test hashCode method - different objects have different hashCode")
    void shouldTestHashCodeMethodDifferentObjects() {
        // Arrange
        Wallet wallet1 = new Wallet("WAL_123", "CLIENT123", 1000.0, "2025-01-01T10:00:00.000Z");
        Wallet wallet2 = new Wallet("WAL_456", "CLIENT456", 2000.0, "2025-01-02T10:00:00.000Z");

        // Act & Assert
        assertNotEquals(wallet1.hashCode(), wallet2.hashCode());
    }

    @Test
    @DisplayName("Should test toString method")
    void shouldTestToStringMethod() {
        // Arrange
        Wallet wallet = new Wallet("WAL_123", "CLIENT123", 1000.0, "2025-01-01T10:00:00.000Z");

        // Act
        String result = wallet.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("WAL_123"));
        assertTrue(result.contains("CLIENT123"));
        assertTrue(result.contains("1000.0"));
        assertTrue(result.contains("2025-01-01T10:00:00.000Z"));
    }
}
