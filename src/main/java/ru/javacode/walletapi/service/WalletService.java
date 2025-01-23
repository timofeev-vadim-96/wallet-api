package ru.javacode.walletapi.service;

import ru.javacode.walletapi.dto.WalletDto;
import ru.javacode.walletapi.util.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {
    WalletDto get(UUID id);

    WalletDto makeTransaction(UUID id, OperationType type, BigDecimal amount);
}
