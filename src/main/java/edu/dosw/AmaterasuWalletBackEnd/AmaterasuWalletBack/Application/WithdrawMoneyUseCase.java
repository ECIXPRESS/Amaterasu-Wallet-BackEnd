package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepository;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Exception.CustomExceptions;
import org.springframework.stereotype.Service;

@Service
public class WithdrawMoneyUseCase implements UseCase<WalletRequest, WalletResponse> {

    private final WalletRepository walletRepository;

    public WithdrawMoneyUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletResponse execute(WalletRequest request) {
        validateWalletRequest(request);
        Wallet wallet = walletRepository.findByClientId(request.getClientId())
                .orElseThrow(() -> new CustomExceptions.WalletNotFoundException("No se encontró billetera para el cliente: " + request.getClientId()));
        try {
            wallet.withdrawMoney(request.getAmount());
        } catch (IllegalStateException e) {
            throw new CustomExceptions.InsufficientFundsException("Fondos insuficientes. Saldo actual: " + wallet.getBigDecimal() + ", Monto a retirar: " + request.getAmount());}
        Wallet updatedWallet = walletRepository.save(wallet);
        return toWalletResponse(updatedWallet);
    }

    private void validateWalletRequest(WalletRequest request) {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new CustomExceptions.InvalidAmountException("El monto debe ser un valor positivo. Valor recibido: " + request.getAmount());}
        if (request.getClientId() == null || request.getClientId().trim().isEmpty()) {
            throw new IllegalArgumentException("ClientId es requerido");}
    }

    private WalletResponse toWalletResponse(Wallet wallet) {
        return new WalletResponse(
                wallet.getWalletId(),
                wallet.getClientId(),
                wallet.getBigDecimal(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt()
        );
    }
}
