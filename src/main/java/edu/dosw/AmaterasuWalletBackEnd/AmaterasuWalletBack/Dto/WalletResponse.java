package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto;

import java.time.LocalDateTime;
import java.text.DecimalFormat;

public class WalletResponse {
    private String walletId;
    private String clientId;
    private String moneyAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WalletResponse() {}

    public WalletResponse(String walletId, String clientId, Double moneyAmount,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.walletId = walletId;
        this.clientId = clientId;
        this.moneyAmount = formatMoney(moneyAmount);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private String formatMoney(Double amount) {
        if (amount == null) return "0";
        try {
            long longAmount = amount.longValue();
            DecimalFormat formatter = new DecimalFormat("#,###");
            formatter.setGroupingSize(3);
            formatter.setGroupingUsed(true);
            return formatter.format(longAmount);
        } catch (Exception e) {
            return String.valueOf(amount.longValue());
        }
    }

    public String getWalletId() { return walletId; }
    public void setWalletId(String walletId) { this.walletId = walletId; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getMoneyAmount() { return moneyAmount; }
    public void setMoneyAmount(String moneyAmount) { this.moneyAmount = moneyAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}