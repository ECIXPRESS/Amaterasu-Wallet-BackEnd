package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto;

import java.time.LocalDateTime;

public class WalletResponse {
    private String walletId;
    private String clientId;
    private Double moneyAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WalletResponse() {}

    public WalletResponse(String walletId, String clientId, Double moneyAmount,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.walletId = walletId;
        this.clientId = clientId;
        this.moneyAmount = moneyAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public String getWalletId() { return walletId; }
    public void setWalletId(String walletId) { this.walletId = walletId; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public Double getMoneyAmount() { return moneyAmount; }
    public void setMoneyAmount(Double moneyAmount) { this.moneyAmount = moneyAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
