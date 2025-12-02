package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.Ports;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests.AddMoneyRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests.CreateWalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests.GetWalletByClientIdRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests.PayWithWalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletResponses.GetWalletByClientIdResponse;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletResponses.PayWithWalletResponse;

public interface WalletUseCases {
    boolean createWallet(CreateWalletRequest request);
    PayWithWalletResponse payWithWallet(PayWithWalletRequest request);
    boolean addMoney(AddMoneyRequest request);
    GetWalletByClientIdResponse getWalletByClientId(GetWalletByClientIdRequest clientId);
}
