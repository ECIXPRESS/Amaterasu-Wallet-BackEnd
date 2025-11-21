package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepository;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Exception.CustomExceptions;
import org.springframework.stereotype.Service;

@Service
public class GetWalletByClientIdUseCase implements UseCase<String, WalletResponse> {

    private final WalletRepository walletRepository;

    public GetWalletByClientIdUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletResponse execute(String clientId) {
        Wallet wallet = walletRepository.findByClientId(clientId).orElseThrow(() -> new CustomExceptions.
                        WalletNotFoundException("No se encontró billetera para el cliente: " + clientId));
        return toWalletResponse(wallet);
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
