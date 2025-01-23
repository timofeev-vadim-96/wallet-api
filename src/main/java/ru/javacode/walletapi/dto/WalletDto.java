package ru.javacode.walletapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "DTO for Wallet entity")
public class WalletDto {
    @Schema(description = "Unique identifier of the wallet", example = "123e4567-e89b-12d3-a456-426655440000")
    private UUID id;

    @Schema(description = "Balance of the wallet", example = "100.00")
    private BigDecimal balance;
}
