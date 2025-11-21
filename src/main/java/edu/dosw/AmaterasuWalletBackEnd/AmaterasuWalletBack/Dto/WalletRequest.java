package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public class WalletRequest {
    private String clientId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, locale = "de")
    private Double amount;

    public WalletRequest() {}

    public WalletRequest(String clientId, Double amount) {
        this.clientId = clientId;
        this.amount = amount;
    }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}