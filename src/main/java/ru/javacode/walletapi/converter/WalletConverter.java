package ru.javacode.walletapi.converter;

import org.springframework.stereotype.Component;
import ru.javacode.walletapi.dto.WalletDto;
import ru.javacode.walletapi.model.Wallet;

@Component
public class WalletConverter {
    public WalletDto convertToDto(Wallet wallet) {
        return new WalletDto(wallet.getId(), wallet.getBalance());
    }
}
