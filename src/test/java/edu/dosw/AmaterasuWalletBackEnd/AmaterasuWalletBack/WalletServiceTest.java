package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.WalletService;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepository;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Exception.CustomExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    private WalletRepository walletRepository;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletRepository = mock(WalletRepository.class);
        walletService = new WalletService(walletRepository);
    }

    @Test
    void createWallet_success_returnsResponseAndSaves() {
        when(walletRepository.existsByClientId("tomas")).thenReturn(false);
        when(walletRepository.save(any(Wallet.class))).thenAnswer(inv -> inv.getArgument(0));
        WalletRequest req = new WalletRequest("tomas", 50.000);
        WalletResponse resp = walletService.createWallet(req);
        assertEquals("tomas", resp.getClientId());
        assertEquals(50.000, resp.getMoneyAmount());
        assertNotNull(resp.getWalletId());
        verify(walletRepository, times(1)).existsByClientId("tomas");
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void createWallet_alreadyExists_throws() {
        when(walletRepository.existsByClientId("felipe")).thenReturn(true);
        WalletRequest req = new WalletRequest("felipe", 10.000);
        assertThrows(CustomExceptions.WalletAlreadyExistsException.class, () -> walletService.createWallet(req));
        verify(walletRepository, times(1)).existsByClientId("felipe");
        verify(walletRepository, never()).save(any());
    }


    @Test
    void addMoney_success_updatesBalance() {
        Wallet wallet = Wallet.createWallet("daniel", 20.000);
        when(walletRepository.findByClientId("daniel")).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(inv -> inv.getArgument(0));
        WalletRequest req = new WalletRequest("daniel", 30.000);
        WalletResponse resp = walletService.addMoney(req);
        assertEquals(50.000, resp.getMoneyAmount());
        verify(walletRepository, times(1)).findByClientId("daniel");
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void addMoney_walletNotFound_throws() {
        when(walletRepository.findByClientId("perdida")).thenReturn(Optional.empty());
        WalletRequest req = new WalletRequest("perdida", 10.000);
        assertThrows(CustomExceptions.WalletNotFoundException.class, () -> walletService.addMoney(req));
        verify(walletRepository, times(1)).findByClientId("perdida");
        verify(walletRepository, never()).save(any());
    }

    @Test
    void withdrawMoney_success_updatesBalance() {
        Wallet wallet = Wallet.createWallet("david", 100.000);
        when(walletRepository.findByClientId("david")).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(inv -> inv.getArgument(0));
        WalletRequest req = new WalletRequest("david", 40.000);
        WalletResponse resp = walletService.withdrawMoney(req);
        assertEquals(60.0, resp.getMoneyAmount());
        verify(walletRepository, times(1)).findByClientId("david");
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void withdrawMoney_walletNotFound_throws() {
        when(walletRepository.findByClientId("perdida2")).thenReturn(Optional.empty());
        WalletRequest req = new WalletRequest("perdida2", 5.000);
        assertThrows(CustomExceptions.WalletNotFoundException.class, () -> walletService.withdrawMoney(req));
        verify(walletRepository, times(1)).findByClientId("perdida2");
        verify(walletRepository, never()).save(any());
    }

    @Test
    void withdrawMoney_insufficientFunds_throwsInsufficientFundsException() {
        Wallet wallet = Wallet.createWallet("camila", 10.000);
        when(walletRepository.findByClientId("camila")).thenReturn(Optional.of(wallet));
        WalletRequest req = new WalletRequest("camila", 20.000);
        assertThrows(CustomExceptions.InsufficientFundsException.class, () -> walletService.withdrawMoney(req));
        verify(walletRepository, times(1)).findByClientId("camila");
        verify(walletRepository, never()).save(any());
    }

    @Test
    void getWalletByClientId_success_returnsResponse() {
        Wallet wallet = Wallet.createWallet("nicoll", 7.000);
        when(walletRepository.findByClientId("nicoll")).thenReturn(Optional.of(wallet));
        WalletResponse resp = walletService.getWalletByClientId("nicoll");
        assertEquals("nicoll", resp.getClientId());
        assertEquals(7.000, resp.getMoneyAmount());
        verify(walletRepository, times(1)).findByClientId("nicoll");
    }

    @Test
    void getWalletByWalletId_notFound_throws() {
        when(walletRepository.findByWalletId("360758")).thenReturn(Optional.empty());
        assertThrows(CustomExceptions.WalletNotFoundException.class, () -> walletService.getWalletByWalletId("360758"));
        verify(walletRepository, times(1)).findByWalletId("360758");
    }
}

