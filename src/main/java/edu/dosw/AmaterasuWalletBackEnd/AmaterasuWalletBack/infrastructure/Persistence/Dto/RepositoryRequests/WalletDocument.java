package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Persistence.Dto.RepositoryRequests;


import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Domain.Model.Wallet;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Wallets")
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDocument {

    @Id
    private String walletId;
    @Field("clientId")
    private String clientId;
    @Field("moneyAmount")
    private double moneyAmount;
    @Field("updatedAt")
    private String updatedAt;

}
