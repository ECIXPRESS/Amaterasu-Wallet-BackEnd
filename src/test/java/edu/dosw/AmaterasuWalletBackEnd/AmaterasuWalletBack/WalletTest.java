package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    @Test
    void createWallet_withPositiveInitialAmount_setsFields() {
        Wallet wallet = new Wallet("123", "tomas", 100.0);
        assertEquals("123", wallet.getWalletId());
        assertEquals("tomas", wallet.getClientId());
        assertEquals(100.0, wallet.getMoneyAmount());
        assertNotNull(wallet.getCreatedAt());
        assertNotNull(wallet.getUpdatedAt());
    }

    @Test
    void createWallet_withZeroInitialAmount_allowed() {
        Wallet wallet = Wallet.createWallet("david", 0.0);
        assertEquals(0.0, wallet.getMoneyAmount());
    }

    @Test
    void createWallet_negativeInitialAmount_throws() {
        assertThrows(IllegalArgumentException.class, () -> Wallet.createWallet("daniel", -5.0));
    }

    @Test
    void createWallet_nullClientId_throws() {
        assertThrows(IllegalArgumentException.class, () -> Wallet.createWallet(null, 10.0));
    }

    @Test
    void addMoney_positive_increasesBalance_and_updatesTimestamp() throws InterruptedException {
        Wallet wallet = Wallet.createWallet("felipe", 50.000);
        LocalDateTime before = wallet.getUpdatedAt();
        Thread.sleep(5);
        wallet.addMoney(25.000);
        assertEquals(75.000, wallet.getMoneyAmount());
        assertTrue(wallet.getUpdatedAt().isAfter(before));
    }

    @Test
    void addMoney_nonPositive_throws() {
        Wallet wallet = Wallet.createWallet("nicoll", 20.000);
        assertThrows(IllegalArgumentException.class, () -> wallet.addMoney(0.0));
        assertThrows(IllegalArgumentException.class, () -> wallet.addMoney(-1.0));
    }

    @Test
    void withdrawMoney_positive_decreasesBalance_and_updatesTimestamp() throws InterruptedException {
        Wallet wallet = Wallet.createWallet("mariana", 100.000);
        LocalDateTime before = wallet.getUpdatedAt();
        Thread.sleep(5);
        wallet.withdrawMoney(40.000);
        assertEquals(60.000, wallet.getMoneyAmount());
        assertTrue(wallet.getUpdatedAt().isAfter(before));
    }

    @Test
    void withdrawMoney_negative_throws() {
        Wallet wallet = Wallet.createWallet("julian", 30.000);
        assertThrows(IllegalArgumentException.class, () -> wallet.withdrawMoney(-5.000));
        assertThrows(IllegalArgumentException.class, () -> wallet.withdrawMoney(0.0));
    }

    @Test
    void withdrawMoney_insufficientFunds_throws() {
        Wallet wallet = Wallet.createWallet("sara", 15.000);
        assertThrows(IllegalStateException.class, () -> wallet.withdrawMoney(20.000));
    }

    @Test
    void explicitConstructor_preservesProvidedTimestamps() {
        LocalDateTime created = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime updated = LocalDateTime.of(2020, 1, 2, 0, 0);
        Wallet wallet = new Wallet("946739TEST", "elizabet", 5.0, created, updated);
        assertEquals(created, wallet.getCreatedAt());
        assertEquals(updated, wallet.getUpdatedAt());
        assertEquals("946739TEST", wallet.getWalletId());
        assertEquals("elizabet", wallet.getClientId());
    }
}

