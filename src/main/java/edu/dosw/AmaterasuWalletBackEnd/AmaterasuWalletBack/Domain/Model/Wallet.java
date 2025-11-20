package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model;

import java.time.LocalDateTime;

public class Wallet {
    private String walletId;
    private String clientId;
    private Double moneyAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Wallet(String walletId, String clientId, Double moneyAmount) {
        this.walletId = walletId;
        this.clientId = clientId;
        this.moneyAmount = moneyAmount;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        validate();
    }

    public Wallet(String walletId, String clientId, Double moneyAmount,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.walletId = walletId;
        this.clientId = clientId;
        this.moneyAmount = moneyAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validate();
    }

    public void addMoney(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a agregar debe ser positivo");
        }
        this.moneyAmount += amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void withdrawMoney(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser positivo");}
        if (amount > this.moneyAmount) {
            throw new IllegalStateException("Fondos insuficientes en la billetera");}
        this.moneyAmount -= amount;
        this.updatedAt = LocalDateTime.now();
    }

    public static Wallet createWallet(String clientId, Double initialAmount) {
        if (initialAmount < 0) {
            throw new IllegalArgumentException("El monto inicial no puede ser negativo");}
        String walletId = generateWalletId();
        return new Wallet(walletId, clientId, initialAmount);
    }

    private static String generateWalletId() {
        return "WAL_" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void validate() {
        if (clientId == null || clientId.trim().isEmpty()) {
            throw new IllegalArgumentException("ClientId es requerido");
        }
        if (moneyAmount < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
    }

    public String getWalletId() { return walletId; }
    public String getClientId() { return clientId; }
    public Double getMoneyAmount() { return moneyAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
