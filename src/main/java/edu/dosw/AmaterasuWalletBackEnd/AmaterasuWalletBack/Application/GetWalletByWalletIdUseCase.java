package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepository;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Exception.CustomExceptions;
import org.springframework.stereotype.Service;

@Service
public class GetWalletByWalletIdUseCase implements UseCase<String, WalletResponse> {

    private final WalletRepository walletRepository;

    public GetWalletByWalletIdUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletResponse execute(String walletId) {
        Wallet wallet = walletRepository.findByWalletId(walletId)
                .orElseThrow(() -> new CustomExceptions.WalletNotFoundException("No se encontró billetera con ID: " + walletId));
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
