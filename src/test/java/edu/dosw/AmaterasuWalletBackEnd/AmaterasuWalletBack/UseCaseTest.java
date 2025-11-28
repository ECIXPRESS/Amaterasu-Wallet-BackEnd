package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.*;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepository;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Exception.CustomExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UseCaseTest {

    @Mock
    private WalletRepository walletRepository;

    private AddMoneyUseCase addMoneyUseCase;
    private CreateWalletUseCase createWalletUseCase;
    private GetWalletByClientIdUseCase getWalletByClientIdUseCase;
    private GetWalletByWalletIdUseCase getWalletByWalletIdUseCase;
    private WithdrawMoneyUseCase withdrawMoneyUseCase;

    private Wallet testWallet;
    private WalletRequest validWalletRequest;
    private String clientId;
    private String walletId;

    @BeforeEach
    void setUp() {
        addMoneyUseCase = new AddMoneyUseCase(walletRepository);
        createWalletUseCase = new CreateWalletUseCase(walletRepository);
        getWalletByClientIdUseCase = new GetWalletByClientIdUseCase(walletRepository);
        getWalletByWalletIdUseCase = new GetWalletByWalletIdUseCase(walletRepository);
        withdrawMoneyUseCase = new WithdrawMoneyUseCase(walletRepository);
        clientId = "client-123";
        walletId = UUID.randomUUID().toString();
        testWallet = new Wallet(walletId, clientId, 100.0, LocalDateTime.now(), LocalDateTime.now());
        validWalletRequest = new WalletRequest(clientId, 50.0);
    }

    @Test
    void addMoneyUseCase_Execute_Success() {
        when(walletRepository.findByClientId(clientId)).thenReturn(Optional.of(testWallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(testWallet);
        WalletResponse response = addMoneyUseCase.execute(validWalletRequest);
        assertNotNull(response);
        assertEquals(walletId, response.getWalletId());
        assertEquals(clientId, response.getClientId());
        verify(walletRepository).findByClientId(clientId);
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    void addMoneyUseCase_Execute_WalletNotFound() {
        when(walletRepository.findByClientId(clientId)).thenReturn(Optional.empty());
        CustomExceptions.WalletNotFoundException exception = assertThrows(
                CustomExceptions.WalletNotFoundException.class, () -> addMoneyUseCase.execute(validWalletRequest));
        assertTrue(exception.getMessage().contains(clientId));
    }

    @Test
    void addMoneyUseCase_Execute_InvalidAmountNull() {
        WalletRequest invalidRequest = new WalletRequest(clientId, null);
        CustomExceptions.InvalidAmountException exception = assertThrows(
                CustomExceptions.InvalidAmountException.class, () -> addMoneyUseCase.execute(invalidRequest));
        assertTrue(exception.getMessage().contains("positivo"));
    }

    @Test
    void addMoneyUseCase_Execute_InvalidAmountZero() {
        WalletRequest invalidRequest = new WalletRequest(clientId, 0.0);
        CustomExceptions.InvalidAmountException exception = assertThrows(
                CustomExceptions.InvalidAmountException.class, () -> addMoneyUseCase.execute(invalidRequest));
        assertTrue(exception.getMessage().contains("positivo"));
    }

    @Test
    void addMoneyUseCase_Execute_InvalidAmountNegative() {
        WalletRequest invalidRequest = new WalletRequest(clientId, -10.0);
        CustomExceptions.InvalidAmountException exception = assertThrows(
                CustomExceptions.InvalidAmountException.class, () -> addMoneyUseCase.execute(invalidRequest));
        assertTrue(exception.getMessage().contains("positivo"));
    }

    @Test
    void addMoneyUseCase_Execute_InvalidClientIdNull() {
        WalletRequest invalidRequest = new WalletRequest(null, 50.0);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> addMoneyUseCase.execute(invalidRequest));
        assertEquals("ClientId es requerido", exception.getMessage());
    }

    @Test
    void addMoneyUseCase_Execute_InvalidClientIdEmpty() {
        WalletRequest invalidRequest = new WalletRequest("", 50.0);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> addMoneyUseCase.execute(invalidRequest));
        assertEquals("ClientId es requerido", exception.getMessage());
    }

    @Test
    void addMoneyUseCase_Execute_InvalidClientIdBlank() {
        WalletRequest invalidRequest = new WalletRequest("   ", 50.0);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> addMoneyUseCase.execute(invalidRequest));
        assertEquals("ClientId es requerido", exception.getMessage());
    }

    @Test
    void createWalletUseCase_Execute_Success() {
        when(walletRepository.existsByClientId(clientId)).thenReturn(false);
        when(walletRepository.save(any(Wallet.class))).thenReturn(testWallet);
        WalletResponse response = createWalletUseCase.execute(validWalletRequest);
        assertNotNull(response);
        verify(walletRepository).existsByClientId(clientId);
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    void createWalletUseCase_Execute_WalletAlreadyExists() {
        when(walletRepository.existsByClientId(clientId)).thenReturn(true);
        CustomExceptions.WalletAlreadyExistsException exception = assertThrows(
                CustomExceptions.WalletAlreadyExistsException.class, () -> createWalletUseCase.execute(validWalletRequest));
        assertTrue(exception.getMessage().contains(clientId));
    }

    @Test
    void createWalletUseCase_Execute_InvalidAmountNegative() {
        WalletRequest invalidRequest = new WalletRequest(clientId, -10.0);
        when(walletRepository.existsByClientId(clientId)).thenReturn(false);
        CustomExceptions.InvalidAmountException exception = assertThrows(
                CustomExceptions.InvalidAmountException.class, () -> createWalletUseCase.execute(invalidRequest));
        assertTrue(exception.getMessage().contains("negativo"));
    }

    @Test
    void getWalletByClientIdUseCase_Execute_Success() {
        when(walletRepository.findByClientId(clientId)).thenReturn(Optional.of(testWallet));
        WalletResponse response = getWalletByClientIdUseCase.execute(clientId);
        assertNotNull(response);
        assertEquals(walletId, response.getWalletId());
        assertEquals(clientId, response.getClientId());
        assertEquals("100", response.getMoneyAmount());
        verify(walletRepository).findByClientId(clientId);
    }

    @Test
    void getWalletByClientIdUseCase_Execute_WalletNotFound() {
        when(walletRepository.findByClientId(clientId)).thenReturn(Optional.empty());
        CustomExceptions.WalletNotFoundException exception = assertThrows(
                CustomExceptions.WalletNotFoundException.class, () -> getWalletByClientIdUseCase.execute(clientId));
        assertTrue(exception.getMessage().contains(clientId));
    }

    @Test
    void getWalletByClientIdUseCase_Execute_WithNullClientId() {
        CustomExceptions.WalletNotFoundException exception = assertThrows(
                CustomExceptions.WalletNotFoundException.class, () -> getWalletByClientIdUseCase.execute(null));
        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    void getWalletByWalletIdUseCase_Execute_Success() {
        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(testWallet));
        WalletResponse response = getWalletByWalletIdUseCase.execute(walletId);
        assertNotNull(response);
        assertEquals(walletId, response.getWalletId());
        assertEquals(clientId, response.getClientId());
        assertEquals("100", response.getMoneyAmount());
        verify(walletRepository).findByWalletId(walletId);
    }

    @Test
    void getWalletByWalletIdUseCase_Execute_WalletNotFound() {
        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.empty());
        CustomExceptions.WalletNotFoundException exception = assertThrows(
                CustomExceptions.WalletNotFoundException.class, () -> getWalletByWalletIdUseCase.execute(walletId));
        assertTrue(exception.getMessage().contains(walletId));
    }

    @Test
    void getWalletByWalletIdUseCase_Execute_WithNullWalletId() {
        CustomExceptions.WalletNotFoundException exception = assertThrows(
                CustomExceptions.WalletNotFoundException.class, () -> getWalletByWalletIdUseCase.execute(null));
        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    void withdrawMoneyUseCase_Execute_Success() {
        when(walletRepository.findByClientId(clientId)).thenReturn(Optional.of(testWallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(testWallet);
        WalletResponse response = withdrawMoneyUseCase.execute(validWalletRequest);
        assertNotNull(response);
        assertEquals(walletId, response.getWalletId());
        assertEquals(clientId, response.getClientId());
        verify(walletRepository).findByClientId(clientId);
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    void withdrawMoneyUseCase_Execute_WalletNotFound() {
        when(walletRepository.findByClientId(clientId)).thenReturn(Optional.empty());
        CustomExceptions.WalletNotFoundException exception = assertThrows(
                CustomExceptions.WalletNotFoundException.class, () -> withdrawMoneyUseCase.execute(validWalletRequest));
        assertTrue(exception.getMessage().contains(clientId));
    }

    @Test
    void withdrawMoneyUseCase_Execute_InsufficientFunds() {
        Wallet walletWithLowBalance = new Wallet(walletId, clientId, 10.0, LocalDateTime.now(), LocalDateTime.now());
        WalletRequest largeWithdrawalRequest = new WalletRequest(clientId, 100.0);
        when(walletRepository.findByClientId(clientId)).thenReturn(Optional.of(walletWithLowBalance));
        CustomExceptions.InsufficientFundsException exception = assertThrows(
                CustomExceptions.InsufficientFundsException.class, () -> withdrawMoneyUseCase.execute(largeWithdrawalRequest));
        assertTrue(exception.getMessage().contains("Fondos insuficientes"));
        assertTrue(exception.getMessage().contains("10.0"));
        assertTrue(exception.getMessage().contains("100.0"));
    }

    @Test
    void withdrawMoneyUseCase_Execute_InvalidAmountNull() {
        WalletRequest invalidRequest = new WalletRequest(clientId, null);
        CustomExceptions.InvalidAmountException exception = assertThrows(
                CustomExceptions.InvalidAmountException.class, () -> withdrawMoneyUseCase.execute(invalidRequest)
        );
        assertTrue(exception.getMessage().contains("positivo"));
    }

    @Test
    void withdrawMoneyUseCase_Execute_InvalidAmountZero() {
        WalletRequest invalidRequest = new WalletRequest(clientId, 0.0);
        CustomExceptions.InvalidAmountException exception = assertThrows(
                CustomExceptions.InvalidAmountException.class, () -> withdrawMoneyUseCase.execute(invalidRequest));
        assertTrue(exception.getMessage().contains("positivo"));
    }

    @Test
    void withdrawMoneyUseCase_Execute_InvalidClientIdNull() {
        WalletRequest invalidRequest = new WalletRequest(null, 50.0);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> withdrawMoneyUseCase.execute(invalidRequest));
        assertEquals("ClientId es requerido", exception.getMessage());
    }

    @Test
    void withdrawMoneyUseCase_Execute_InvalidClientIdEmpty() {
        WalletRequest invalidRequest = new WalletRequest("", 50.0);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> withdrawMoneyUseCase.execute(invalidRequest));
        assertEquals("ClientId es requerido", exception.getMessage());
    }

    @Test
    void allUseCases_Constructor_Injection() {
        assertNotNull(addMoneyUseCase);
        assertNotNull(createWalletUseCase);
        assertNotNull(getWalletByClientIdUseCase);
        assertNotNull(getWalletByWalletIdUseCase);
        assertNotNull(withdrawMoneyUseCase);
    }

    @Test
    void walletResponse_ConstructorAndGetters_WorkCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        WalletResponse response = new WalletResponse(walletId, clientId, 100.0, now, now);
        assertEquals(walletId, response.getWalletId());
        assertEquals(clientId, response.getClientId());
        assertEquals("100", response.getMoneyAmount());
        assertEquals(now, response.getCreatedAt());
        assertEquals(now, response.getUpdatedAt());
    }

    @Test
    void walletRequest_ConstructorAndGetters_WorkCorrectly() {
        WalletRequest request = new WalletRequest(clientId, 50.0);
        assertEquals(clientId, request.getClientId());
        assertEquals(50.0, request.getAmount());
    }

    @Test
    void useCaseInterface_CanBeImplemented() {
        UseCase<String, String> simpleUseCase = new UseCase<String, String>() {
            @Override
            public String execute(String input) {
                return "processed: " + input;}
        };
        String result = simpleUseCase.execute("test");
        assertEquals("processed: test", result);
    }
}