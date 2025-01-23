package ru.javacode.walletapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.javacode.walletapi.util.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "DTO for Transaction entity")
public class TransactionDto {
    @Schema(description = "Unique identifier of the wallet", example = "123e4567-e89b-12d3-a456-426655440000")
    @NotNull(message = "Id must not be null")
    private UUID walletId;

    @Schema(description = "Type of the operation")
    @NotNull(message = "Operation type must not be null")
    private OperationType operationType;

    @Schema(description = "The amount to make the transaction for", example = "100.00")
    @NotNull(message = "Amount must not be null")
    private BigDecimal amount;
}
