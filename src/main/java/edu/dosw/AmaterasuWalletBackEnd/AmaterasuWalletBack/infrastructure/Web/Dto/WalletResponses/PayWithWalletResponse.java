package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletResponses;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Enums.PaymentStatus;

public record PayWithWalletResponse (
        PaymentStatus paymentStatus
){
}
