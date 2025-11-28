package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepository;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Exception.CustomExceptions;
import org.springframework.stereotype.Service;

@Service
public class CreateWalletUseCase implements UseCase<WalletRequest, WalletResponse> {

    private final WalletRepository walletRepository;

    public CreateWalletUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletResponse execute(WalletRequest request) {
        if (walletRepository.existsByClientId(request.getClientId())) {
            throw new CustomExceptions.WalletAlreadyExistsException("Ya existe una billetera para el cliente: " + request.getClientId());}
        if (request.getAmount() == null || request.getAmount() < 0) {
            throw new CustomExceptions.InvalidAmountException("El monto inicial no puede ser negativo. Valor recibido: " + request.getAmount());}
        Wallet newWallet = Wallet.createWallet(
                request.getClientId(),
                request.getAmount() != null ? request.getAmount() : 0.0
        );

        Wallet savedWallet = walletRepository.save(newWallet);
        return toWalletResponse(savedWallet);
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