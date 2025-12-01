package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Enums;

public enum PaymentStatus {
    PENDING,
    VALIDATING,
    PROCESSING,
    COMPLETED,
    FAILED,
    REFUNDED,
    CANCELLED,
    TIMEOUT
}
