package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.WalletService;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletResponse;
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
    private WalletService walletService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        walletService = mock(WalletService.class);
        WalletController controller = new WalletController(walletService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    private WalletResponse sampleResponse() {
        return new WalletResponse("23534", "tomas", 100.0, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void createWallet_success() throws Exception {
        WalletResponse resp = sampleResponse();
        when(walletService.createWallet(any(WalletRequest.class))).thenReturn(resp);
        WalletRequest req = new WalletRequest("tomas", 100.0);
        mockMvc.perform(post("/api/wallets/create")
                .contentType("application/json").content(objectMapper.writeValueAsString(req))).andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId", is(resp.getWalletId())))
                .andExpect(jsonPath("$.clientId", is(resp.getClientId())))
                .andExpect(jsonPath("$.moneyAmount", is(resp.getMoneyAmount())));
        ArgumentCaptor<WalletRequest> captor = ArgumentCaptor.forClass(WalletRequest.class);
        verify(walletService, times(1)).createWallet(captor.capture());
        assertEquals("tomas", captor.getValue().getClientId());
    }

    @Test
    void addMoney_success() throws Exception {
        WalletResponse resp = new WalletResponse("5678", "felipe", 150.0, LocalDateTime.now(), LocalDateTime.now());
        when(walletService.addMoney(any(WalletRequest.class))).thenReturn(resp);
        WalletRequest req = new WalletRequest("felipe", 50.0);
        mockMvc.perform(post("/api/wallets/add-money")
                .contentType("application/json").content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.moneyAmount", is(resp.getMoneyAmount())));
        verify(walletService, times(1)).addMoney(any(WalletRequest.class));
    }

    @Test
    void withdrawMoney_success() throws Exception {
        WalletResponse resp = new WalletResponse("78365", "david", 30.0, LocalDateTime.now(), LocalDateTime.now());
        when(walletService.withdrawMoney(any(WalletRequest.class))).thenReturn(resp);
        WalletRequest req = new WalletRequest("david", 20.0);
        mockMvc.perform(post("/api/wallets/withdraw-money")
                .contentType("application/json").content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.walletId", is(resp.getWalletId())));
        verify(walletService, times(1)).withdrawMoney(any(WalletRequest.class));
    }

    @Test
    void getWalletByClientId_success() throws Exception {
        WalletResponse resp = sampleResponse();
        when(walletService.getWalletByClientId(eq("tomas"))).thenReturn(resp);
        mockMvc.perform(get("/api/wallets/client/tomas"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.clientId", is("tomas")));
        verify(walletService, times(1)).getWalletByClientId("tomas");
    }

    @Test
    void getWalletByWalletId_success() throws Exception {
        WalletResponse resp = sampleResponse();
        when(walletService.getWalletByWalletId(eq("23534"))).thenReturn(resp);
        mockMvc.perform(get("/api/wallets/23534")).andExpect(status().isOk()).andExpect(jsonPath("$.walletId", is("23534")));
        verify(walletService, times(1)).getWalletByWalletId("23534");
    }

    @Test
    void createWallet_malformedJson_returnsBadRequest() throws Exception {
        String badJson = "{ bad json }";
        mockMvc.perform(post("/api/wallets/create")
                .contentType("application/json").content(badJson)).andExpect(status().isBadRequest());
        verify(walletService, times(0)).createWallet(any());
    }
}

