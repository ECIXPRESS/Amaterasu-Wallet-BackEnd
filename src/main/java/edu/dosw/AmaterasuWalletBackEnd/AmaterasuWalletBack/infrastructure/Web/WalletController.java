package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.*;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.UseCase.*;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final CreateWalletUseCase createWalletUseCase;
    private final AddMoneyUseCase addMoneyUseCase;
    private final WithdrawMoneyUseCase withdrawMoneyUseCase;
    private final GetWalletByClientIdUseCase getWalletByClientIdUseCase;
    private final GetWalletByWalletIdUseCase getWalletByWalletIdUseCase;

    public WalletController(CreateWalletUseCase createWalletUseCase,
                            AddMoneyUseCase addMoneyUseCase,
                            WithdrawMoneyUseCase withdrawMoneyUseCase,
                            GetWalletByClientIdUseCase getWalletByClientIdUseCase,
                            GetWalletByWalletIdUseCase getWalletByWalletIdUseCase) {
        this.createWalletUseCase = createWalletUseCase;
        this.addMoneyUseCase = addMoneyUseCase;
        this.withdrawMoneyUseCase = withdrawMoneyUseCase;
        this.getWalletByClientIdUseCase = getWalletByClientIdUseCase;
        this.getWalletByWalletIdUseCase = getWalletByWalletIdUseCase;
    }

    @Operation(summary = "Crear una nueva billetera para un cliente")
    @PostMapping("/create")
    public ResponseEntity<WalletResponse> createWallet(@RequestBody WalletRequest request) {
        WalletResponse response = createWalletUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Agregar dinero al saldo de la billetera")
    @PostMapping("/add-money")
    public ResponseEntity<WalletResponse> addMoney(@RequestBody WalletRequest request) {
        WalletResponse response = addMoneyUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retirar dinero del saldo de la billetera")
    @PostMapping("/withdraw-money")
    public ResponseEntity<WalletResponse> withdrawMoney(@RequestBody WalletRequest request) {
        WalletResponse response = withdrawMoneyUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar billetera por ID del cliente")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<WalletResponse> getWalletByClientId(@PathVariable String clientId) {
        WalletResponse response = getWalletByClientIdUseCase.execute(clientId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar billetera por ID de la billetera")
    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponse> getWalletByWalletId(@PathVariable String walletId) {
        WalletResponse response = getWalletByWalletIdUseCase.execute(walletId);
        return ResponseEntity.ok(response);
    }
}