package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.*;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.UseCase.*;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Exception.CustomExceptions;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Exception.GlobalExceptionHandler;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.WalletController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WalletControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CreateWalletUseCase createWalletUseCase;
    private AddMoneyUseCase addMoneyUseCase;
    private WithdrawMoneyUseCase withdrawMoneyUseCase;
    private GetWalletByClientIdUseCase getWalletByClientIdUseCase;
    private GetWalletByWalletIdUseCase getWalletByWalletIdUseCase;

    @BeforeEach
    void setUp() {
        createWalletUseCase = mock(CreateWalletUseCase.class);
        addMoneyUseCase = mock(AddMoneyUseCase.class);
        withdrawMoneyUseCase = mock(WithdrawMoneyUseCase.class);
        getWalletByClientIdUseCase = mock(GetWalletByClientIdUseCase.class);
        getWalletByWalletIdUseCase = mock(GetWalletByWalletIdUseCase.class);
        WalletController controller = new WalletController(
                createWalletUseCase,
                addMoneyUseCase,
                withdrawMoneyUseCase,
                getWalletByClientIdUseCase,
                getWalletByWalletIdUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    private WalletResponse sampleResponse() {
        return new WalletResponse("23534", "tomas", 100.0, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void createWallet_success() throws Exception {
        WalletResponse resp = sampleResponse();
        when(createWalletUseCase.execute(any(WalletRequest.class))).thenReturn(resp);
        WalletRequest req = new WalletRequest("tomas", 100.0);
        mockMvc.perform(post("/api/wallets/create")
                        .contentType("application/json").content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId", is(resp.getWalletId())))
                .andExpect(jsonPath("$.clientId", is(resp.getClientId())))
                .andExpect(jsonPath("$.moneyAmount", is(resp.getMoneyAmount())));
        ArgumentCaptor<WalletRequest> captor = ArgumentCaptor.forClass(WalletRequest.class);
        verify(createWalletUseCase, times(1)).execute(captor.capture());
        assertEquals("tomas", captor.getValue().getClientId());
        assertEquals(100.0, captor.getValue().getAmount());
    }

    @Test
    void addMoney_success() throws Exception {
        WalletResponse resp = new WalletResponse("5678", "felipe", 150.0, LocalDateTime.now(), LocalDateTime.now());
        when(addMoneyUseCase.execute(any(WalletRequest.class))).thenReturn(resp);
        WalletRequest req = new WalletRequest("felipe", 50.0);
        mockMvc.perform(post("/api/wallets/add-money").contentType("application/json").content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.moneyAmount", is(resp.getMoneyAmount())));
        verify(addMoneyUseCase, times(1)).execute(any(WalletRequest.class));
    }

    @Test
    void withdrawMoney_success() throws Exception {
        WalletResponse resp = new WalletResponse("78365", "david", 30.0, LocalDateTime.now(), LocalDateTime.now());
        when(withdrawMoneyUseCase.execute(any(WalletRequest.class))).thenReturn(resp);
        WalletRequest req = new WalletRequest("david", 20.0);
        mockMvc.perform(post("/api/wallets/withdraw-money").contentType("application/json").content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.walletId", is(resp.getWalletId())));
        verify(withdrawMoneyUseCase, times(1)).execute(any(WalletRequest.class));
    }

    @Test
    void getWalletByClientId_success() throws Exception {
        WalletResponse resp = sampleResponse();
        when(getWalletByClientIdUseCase.execute(eq("tomas"))).thenReturn(resp);
        mockMvc.perform(get("/api/wallets/client/tomas")).andExpect(status().isOk()).andExpect(jsonPath("$.clientId", is("tomas")));
        verify(getWalletByClientIdUseCase, times(1)).execute("tomas");
    }

    @Test
    void getWalletByWalletId_success() throws Exception {
        WalletResponse resp = sampleResponse();
        when(getWalletByWalletIdUseCase.execute(eq("23534"))).thenReturn(resp);
        mockMvc.perform(get("/api/wallets/23534")).andExpect(status().isOk()).andExpect(jsonPath("$.walletId", is("23534")));
        verify(getWalletByWalletIdUseCase, times(1)).execute("23534");
    }

    @Test
    void createWallet_malformedJson_returnsBadRequest() throws Exception {
        String badJson = "{ bad json }";
        mockMvc.perform(post("/api/wallets/create").contentType("application/json").content(badJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Solicitud JSON Inválida")))
                .andExpect(jsonPath("$.message", is("El formato del JSON es incorrecto. Verifique la sintaxis.")));
        verify(createWalletUseCase, times(0)).execute(any());
    }

    @Test
    void getWalletByClientId_notFound() throws Exception {
        when(getWalletByClientIdUseCase.execute(eq("unknown")))
                .thenThrow(new CustomExceptions.WalletNotFoundException("No encontrado"));
        mockMvc.perform(get("/api/wallets/client/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Wallet No Encontrada")))
                .andExpect(jsonPath("$.message", is("No encontrado")));
        verify(getWalletByClientIdUseCase, times(1)).execute("unknown");
    }

    @Test
    void addMoney_invalidAmount() throws Exception {
        when(addMoneyUseCase.execute(any(WalletRequest.class))).thenThrow(new CustomExceptions.InvalidAmountException("Monto inválido"));
        WalletRequest req = new WalletRequest("test", -50.0);
        mockMvc.perform(post("/api/wallets/add-money").contentType("application/json").content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Monto Inválido")))
                .andExpect(jsonPath("$.message", is("Monto inválido")));
        verify(addMoneyUseCase, times(1)).execute(any(WalletRequest.class));
    }

    @Test
    void withdrawMoney_insufficientFunds() throws Exception {
        when(withdrawMoneyUseCase.execute(any(WalletRequest.class))).thenThrow(new CustomExceptions.InsufficientFundsException("Fondos insuficientes"));
        WalletRequest req = new WalletRequest("test", 100.0);
        mockMvc.perform(post("/api/wallets/withdraw-money")
                        .contentType("application/json").content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Fondos Insuficientes")))
                .andExpect(jsonPath("$.message", is("Fondos insuficientes")));
        verify(withdrawMoneyUseCase, times(1)).execute(any(WalletRequest.class));
    }

    @Test
    void createWallet_alreadyExists() throws Exception {
        when(createWalletUseCase.execute(any(WalletRequest.class))).thenThrow(new CustomExceptions.WalletAlreadyExistsException("Ya existe"));
        WalletRequest req = new WalletRequest("test", 100.0);
        mockMvc.perform(post("/api/wallets/create")
                        .contentType("application/json").content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Wallet Ya Existe")))
                .andExpect(jsonPath("$.message", is("Ya existe")));
        verify(createWalletUseCase, times(1)).execute(any(WalletRequest.class));
    }
}