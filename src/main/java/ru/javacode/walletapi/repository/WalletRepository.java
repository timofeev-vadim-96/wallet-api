package ru.javacode.walletapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javacode.walletapi.model.Wallet;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
