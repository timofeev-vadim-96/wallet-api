package ru.javacode.walletapi.converter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.javacode.walletapi.dto.WalletDto;
import ru.javacode.walletapi.model.Wallet;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Wallet converter")
class WalletConverterTest {
    private static WalletConverter converter;

    @BeforeAll
    static void init() {
        converter = new WalletConverter();
    }

    @Test
    void convertToDto() {
        Wallet wallet = new Wallet(UUID.randomUUID(), new BigDecimal("100.00"), null);

        WalletDto dto = converter.convertToDto(wallet);

        assertThat(dto).isNotNull()
                .hasFieldOrPropertyWithValue("id", wallet.getId())
                .hasFieldOrPropertyWithValue("balance", wallet.getBalance());
    }
}