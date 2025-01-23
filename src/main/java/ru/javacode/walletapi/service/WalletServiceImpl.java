package ru.javacode.walletapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javacode.walletapi.converter.WalletConverter;
import ru.javacode.walletapi.dto.WalletDto;
import ru.javacode.walletapi.exception.EntityNotFoundException;
import ru.javacode.walletapi.exception.InsufficientFundsException;
import ru.javacode.walletapi.model.Wallet;
import ru.javacode.walletapi.repository.WalletRepository;
import ru.javacode.walletapi.util.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    private final WalletConverter walletConverter;

    @Override
    @Transactional(readOnly = true)
    public WalletDto get(UUID id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet with id = %s is not found".formatted(id)));

        return walletConverter.convertToDto(wallet);
    }

    @Override
    @Transactional
    @Retryable(
            maxAttempts = 40,
            retryFor = Exception.class,
            noRetryFor = {EntityNotFoundException.class, InsufficientFundsException.class},
            backoff = @Backoff(delay = 100))
    public WalletDto makeTransaction(UUID id, OperationType type, BigDecimal amount) {
        return type.equals(OperationType.DEPOSIT)
                ? deposit(id, amount)
                : withdraw(id, amount);
    }

    private WalletDto deposit(UUID id, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet with id = %s is not found".formatted(id)));

        wallet.setBalance(wallet.getBalance().add(amount));

        return walletConverter.convertToDto(walletRepository.save(wallet));
    }

    private WalletDto withdraw(UUID id, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet with id = %s is not found".formatted(id)));

        if (wallet.getBalance().compareTo(amount) == -1) {
            throw new InsufficientFundsException("On a wallet with an id = %s insufficient funds");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));

        return walletConverter.convertToDto(walletRepository.save(wallet));
    }
}
