package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.Ports.WalletUseCases;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Enums.PaymentStatus;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests.*;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletResponses.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalletUseCases walletUseCases;

    @Test
    @DisplayName("Should create wallet and return 201")
    void shouldCreateWalletAndReturn201() throws Exception {
     
        CreateWalletRequest request = new CreateWalletRequest("CLIENT123", 1000.0);
        when(walletUseCases.createWallet(any())).thenReturn(true);

      
        mockMvc.perform(post("/api/v1/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return 500 when create wallet fails")
    void shouldReturn500WhenCreateWalletFails() throws Exception {
    
        CreateWalletRequest request = new CreateWalletRequest("CLIENT123", 1000.0);
        when(walletUseCases.createWallet(any())).thenReturn(false);

        
        mockMvc.perform(post("/api/v1/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should add money and return 200")
    void shouldAddMoneyAndReturn200() throws Exception {
       
        AddMoneyRequest request = new AddMoneyRequest("CLIENT123", 500.0);
        when(walletUseCases.addMoney(any())).thenReturn(true);

        
        mockMvc.perform(post("/api/v1/wallets/add-money")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 500 when add money fails")
    void shouldReturn500WhenAddMoneyFails() throws Exception {
        
        AddMoneyRequest request = new AddMoneyRequest("CLIENT123", 500.0);
        when(walletUseCases.addMoney(any())).thenReturn(false);

        
        mockMvc.perform(post("/api/v1/wallets/add-money")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should pay with wallet and return 200")
    void shouldPayWithWalletAndReturn200() throws Exception {
    
        PayWithWalletRequest request = new PayWithWalletRequest("CLIENT123", 300.0);
        PayWithWalletResponse response = new PayWithWalletResponse(PaymentStatus.COMPLETED);
        when(walletUseCases.payWithWallet(any())).thenReturn(response);

       
        mockMvc.perform(post("/api/v1/wallets/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentStatus").value("COMPLETED"));
    }

    @Test
    @DisplayName("Should get wallet by client ID and return 200")
    void shouldGetWalletByClientIdAndReturn200() throws Exception {
     
        GetWalletByClientIdResponse response = new GetWalletByClientIdResponse(
            "WAL_123",
            "CLIENT123",
            1000.0,
            "2025-01-01T10:00:00.000Z"
        );
        when(walletUseCases.getWalletByClientId(any())).thenReturn(response);

      
        mockMvc.perform(get("/api/v1/wallets/client/CLIENT123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value("WAL_123"))
                .andExpect(jsonPath("$.clientId").value("CLIENT123"))
                .andExpect(jsonPath("$.moneyAmount").value(1000.0));
    }

    @Test
    @DisplayName("Should return 404 when wallet not found")
    void shouldReturn404WhenWalletNotFound() throws Exception {
       
        when(walletUseCases.getWalletByClientId(any())).thenReturn(null);

      
        mockMvc.perform(get("/api/v1/wallets/client/CLIENT999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException and return 400")
    void shouldHandleIllegalArgumentExceptionAndReturn400() throws Exception {
        
        CreateWalletRequest request = new CreateWalletRequest("", 1000.0);
        when(walletUseCases.createWallet(any()))
            .thenThrow(new IllegalArgumentException("ClientId es requerido"));

      
        mockMvc.perform(post("/api/v1/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ClientId es requerido"));
    }

    @Test
    @DisplayName("Should handle IllegalStateException and return 400")
    void shouldHandleIllegalStateExceptionAndReturn400() throws Exception {
      
        PayWithWalletRequest request = new PayWithWalletRequest("CLIENT123", 2000.0);
        when(walletUseCases.payWithWallet(any()))
            .thenThrow(new IllegalStateException("Fondos insuficientes"));

     
        mockMvc.perform(post("/api/v1/wallets/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fondos insuficientes"));
    }

    @Test
    @DisplayName("Should handle generic Exception and return 500")
    void shouldHandleGenericExceptionAndReturn500() throws Exception {
        
        CreateWalletRequest request = new CreateWalletRequest("CLIENT123", 1000.0);
        when(walletUseCases.createWallet(any()))
            .thenThrow(new RuntimeException("Unexpected error"));

        
        mockMvc.perform(post("/api/v1/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("An unexpected error occurred")));
    }
}
