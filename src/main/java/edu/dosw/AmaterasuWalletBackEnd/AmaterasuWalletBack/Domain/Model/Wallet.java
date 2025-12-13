package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;

import static edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Utils.DateUtils.TIMESTAMP_FORMAT;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    private String walletId;
    private String clientId;
    private double moneyAmount;
    private String updatedAt;

    public Wallet createWallet(String clientId, double moneyAmount) {
        this.walletId = "WAL_" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        if (clientId == null || clientId.trim().isEmpty()) {
            throw new IllegalArgumentException("ClientId es requerido");
        }
        this.clientId = clientId;
        if (moneyAmount < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        this.moneyAmount = moneyAmount;
        this.updatedAt = DateUtils.formatDate(new Date(), TIMESTAMP_FORMAT);
        return this;
    }

    public void addMoney(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a agregar debe ser positivo");
        }
        this.moneyAmount += amount;
        this.updatedAt = DateUtils.formatDate(new Date(), TIMESTAMP_FORMAT);
    }

    public void withdrawMoney(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser positivo");
        }
        if (amount >  this.moneyAmount) {
            throw new IllegalStateException("Fondos insuficientes en la billetera");
        }
        this.moneyAmount -= amount;
        this.updatedAt = DateUtils.formatDate(new Date(), TIMESTAMP_FORMAT);
    }
}