package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepository;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Exception.CustomExceptions;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletResponse createWallet(WalletRequest request) {
        if (walletRepository.existsByClientId(request.getClientId())) {
            throw new CustomExceptions.WalletAlreadyExistsException(
                    "Ya existe una billetera para el cliente: " + request.getClientId());}
        if (request.getAmount() == null || request.getAmount() < 0) {
            throw new CustomExceptions.InvalidAmountException(
                    "El monto inicial no puede ser negativo. Valor recibido: " + request.getAmount());}
        Wallet newWallet = Wallet.createWallet(request.getClientId(), request.getAmount() != null ? request.getAmount() : 0.0);
        Wallet savedWallet = walletRepository.save(newWallet);
        return toWalletResponse(savedWallet);
    }

    public WalletResponse addMoney(WalletRequest request) {
        validateWalletRequest(request);
        Wallet wallet = walletRepository.findByClientId(request.getClientId())
                .orElseThrow(() -> new CustomExceptions.WalletNotFoundException(
                        "No se encontró billetera para el cliente: " + request.getClientId()));
        wallet.addMoney(request.getAmount());
        Wallet updatedWallet = walletRepository.save(wallet);
        return toWalletResponse(updatedWallet);
    }

    public WalletResponse withdrawMoney(WalletRequest request) {
        validateWalletRequest(request);
        Wallet wallet = walletRepository.findByClientId(request.getClientId())
                .orElseThrow(() -> new CustomExceptions.WalletNotFoundException(
                        "No se encontró billetera para el cliente: " + request.getClientId()));
        try {
            wallet.withdrawMoney(request.getAmount());
        } catch (IllegalStateException e) {
            throw new CustomExceptions.InsufficientFundsException(
                    "Fondos insuficientes. Saldo actual: " + wallet.getMoneyAmount() +
                            ", Monto a retirar: " + request.getAmount()
            );
        }
        Wallet updatedWallet = walletRepository.save(wallet);
        return toWalletResponse(updatedWallet);
    }

    public WalletResponse getWalletByClientId(String clientId) {
        Wallet wallet = walletRepository.findByClientId(clientId)
                .orElseThrow(() -> new CustomExceptions.WalletNotFoundException(
                        "No se encontró billetera para el cliente: " + clientId));
        return toWalletResponse(wallet);
    }

    public WalletResponse getWalletByWalletId(String walletId) {
        Wallet wallet = walletRepository.findByWalletId(walletId)
                .orElseThrow(() -> new CustomExceptions.WalletNotFoundException(
                        "No se encontró billetera con ID: " + walletId));
        return toWalletResponse(wallet);
    }

    private void validateWalletRequest(WalletRequest request) {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new CustomExceptions.InvalidAmountException(
                    "El monto debe ser un valor positivo. Valor recibido: " + request.getAmount());
        }
        if (request.getClientId() == null || request.getClientId().trim().isEmpty()) {
            throw new IllegalArgumentException("ClientId es requerido");
        }
    }

    private WalletResponse toWalletResponse(Wallet wallet) {
        return new WalletResponse(wallet.getWalletId(), wallet.getClientId(), wallet.getMoneyAmount(),
                wallet.getCreatedAt(), wallet.getUpdatedAt());
    }
}