package ru.javacode.walletapi.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import ru.javacode.walletapi.model.Wallet;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    @Override
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Wallet save(Wallet wallet);
}
