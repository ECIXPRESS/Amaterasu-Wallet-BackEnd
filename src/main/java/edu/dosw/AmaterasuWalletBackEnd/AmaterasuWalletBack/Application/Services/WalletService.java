package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.Services;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.Mappers.ResponseMapper;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.Ports.WalletUseCases;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Enums.PaymentStatus;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Port.WalletRepositoryProvider;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests.AddMoneyRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests.CreateWalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests.GetWalletByClientIdRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests.PayWithWalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletResponses.GetWalletByClientIdResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletResponses.PayWithWalletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletService implements WalletUseCases {
    private final WalletRepositoryProvider walletRepositoryProvider;


    @Override
    public boolean createWallet(CreateWalletRequest request) {
        Wallet wallet = new Wallet().createWallet(request.clientId(), request.moneyAmount());
        try{
            walletRepositoryProvider.save(wallet);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public PayWithWalletResponse payWithWallet(PayWithWalletRequest request) {
        try{
            Wallet wallet = ResponseMapper.responseToWallet(walletRepositoryProvider.getByClientId(request.clientId()));
            wallet.withdrawMoney(request.moneyAmount());
            walletRepositoryProvider.save(wallet);
            return new PayWithWalletResponse(PaymentStatus.COMPLETED);
        }catch (Exception e){
            return new PayWithWalletResponse(PaymentStatus.FAILED);
        }
    }

    @Override
    public boolean addMoney(AddMoneyRequest request) {
        try{
            Wallet wallet = ResponseMapper.responseToWallet(walletRepositoryProvider.getByClientId(request.clientId()));
            wallet.addMoney(request.moneyAmount());
            walletRepositoryProvider.save(wallet);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public GetWalletByClientIdResponse getWalletByClientId(GetWalletByClientIdRequest request) {
        try{
            Wallet wallet = ResponseMapper.responseToWallet(walletRepositoryProvider.getByClientId(request.clientId()));
            return new GetWalletByClientIdResponse(wallet.getWalletId(), wallet.getClientId(), wallet.getMoneyAmount(), wallet.getUpdatedAt());
        }catch (Exception e){
            return null;
        }
    }
}
