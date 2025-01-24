package ru.javacode.walletapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.javacode.walletapi.dto.TransactionDto;
import ru.javacode.walletapi.dto.WalletDto;
import ru.javacode.walletapi.service.WalletService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Wallet controller", description = "A controller for working with wallets")
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/api/v1/wallet")
    @Operation(description = "Making a transaction with the wallet balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The transaction was completed successfully",
                    content = {@Content(schema = @Schema(implementation = WalletDto.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "There are not enough funds in the wallet"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<WalletDto> post(@Valid @RequestBody TransactionDto transaction) {
        WalletDto wallet = walletService.makeTransaction(
                transaction.getWalletId(), transaction.getOperationType(), transaction.getAmount());

        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @GetMapping("/api/v1/wallet/{id}")
    @Operation(description = "Getting the wallet balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wallet found",
                    content = {@Content(schema = @Schema(implementation = WalletDto.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<WalletDto> get(@NotNull @PathVariable(name = "id") UUID id) {
        WalletDto wallet = walletService.get(id);

        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }
}
