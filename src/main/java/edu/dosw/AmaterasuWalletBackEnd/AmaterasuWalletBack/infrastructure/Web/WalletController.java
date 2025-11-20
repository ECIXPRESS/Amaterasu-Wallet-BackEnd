package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.WalletService;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletRequest;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Dto.WalletResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")

public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(summary = "Crear una nueva billetera para un cliente")
    @PostMapping("/create")
    public ResponseEntity<WalletResponse> createWallet(@RequestBody WalletRequest request) {
        WalletResponse response = walletService.createWallet(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Agregar dinero al saldo de la billetera")
    @PostMapping("/add-money")
    public ResponseEntity<WalletResponse> addMoney(@RequestBody WalletRequest request) {
        WalletResponse response = walletService.addMoney(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retirar dinero del saldo de la billetera")
    @PostMapping("/withdraw-money")
    public ResponseEntity<WalletResponse> withdrawMoney(@RequestBody WalletRequest request) {
        WalletResponse response = walletService.withdrawMoney(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar billetera por ID del cliente")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<WalletResponse> getWalletByClientId(@PathVariable String clientId) {
        WalletResponse response = walletService.getWalletByClientId(clientId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar billetera por ID de la billetera")
    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponse> getWalletByWalletId(@PathVariable String walletId) {
        WalletResponse response = walletService.getWalletByWalletId(walletId);
        return ResponseEntity.ok(response);
    }
}
