package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web;

import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application.Ports.WalletUseCases;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletRequests.*;
import edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.infrastructure.Web.Dto.WalletResponses.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Wallet Management", description = "APIs for managing wallet operations")
@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletUseCases walletUseCases;

    @Operation(summary = "Create a new wallet for a client")
    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody CreateWalletRequest request) {
        boolean isCreated = walletUseCases.createWallet(request);
        return isCreated
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Operation(summary = "Add money to wallet")
    @PostMapping("/add-money")
    public ResponseEntity<Void> addMoney(@RequestBody AddMoneyRequest request) {
        boolean isAdded = walletUseCases.addMoney(request);
        return isAdded
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Operation(summary = "Make a payment using wallet")
    @PostMapping("/pay")
    public ResponseEntity<PayWithWalletResponse> payWithWallet(@RequestBody PayWithWalletRequest request) {
        PayWithWalletResponse response = walletUseCases.payWithWallet(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get wallet by client ID")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<GetWalletByClientIdResponse> getWalletByClientId(@PathVariable String clientId) {
        GetWalletByClientIdRequest request = new GetWalletByClientIdRequest(clientId);
        GetWalletByClientIdResponse response = walletUseCases.getWalletByClientId(request);
        return response != null
                ? ResponseEntity.ok(response)
                : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}