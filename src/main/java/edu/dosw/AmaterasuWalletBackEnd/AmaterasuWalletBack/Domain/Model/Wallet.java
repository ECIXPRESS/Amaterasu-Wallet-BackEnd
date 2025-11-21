package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "wallets")
public class Wallet {

    @Id
    private String walletId;
    private String clientId;
    private Double BigDecimal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Wallet(String walletId, String clientId, Double BigDecimal) {
        this.walletId = walletId;
        this.clientId = clientId;
        this.BigDecimal = BigDecimal;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        validate();
    }

    public Wallet(String walletId, String clientId, Double BigDecimal,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.walletId = walletId;
        this.clientId = clientId;
        this.BigDecimal = BigDecimal;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validate();
    }

    public void addMoney(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a agregar debe ser positivo");
        }
        this.BigDecimal += amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void withdrawMoney(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser positivo");}
        if (amount > this.BigDecimal) {
            throw new IllegalStateException("Fondos insuficientes en la billetera");}
        this.BigDecimal -= amount;
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
        if (BigDecimal < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
    }

    public String getWalletId() { return walletId; }
    public String getClientId() { return clientId; }
    public Double getBigDecimal() { return BigDecimal; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
