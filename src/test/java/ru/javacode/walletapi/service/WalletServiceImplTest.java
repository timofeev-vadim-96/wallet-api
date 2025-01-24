package ru.javacode.walletapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javacode.walletapi.converter.WalletConverter;
import ru.javacode.walletapi.dto.WalletDto;
import ru.javacode.walletapi.exception.EntityNotFoundException;
import ru.javacode.walletapi.exception.InsufficientFundsException;
import ru.javacode.walletapi.model.Wallet;
import ru.javacode.walletapi.repository.WalletRepository;
import ru.javacode.walletapi.util.OperationType;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Wallet service")
@SpringBootTest(classes = {WalletServiceImpl.class, WalletConverter.class})
@Transactional(propagation = Propagation.NEVER)
class WalletServiceImplTest {
    @Autowired
    private WalletService walletService;

    @MockBean
    private WalletRepository walletRepository;

    @SpyBean
    private WalletConverter walletConverter;

    private Wallet wallet;

    @BeforeEach
    void stub() {
        wallet = new Wallet(UUID.randomUUID(), new BigDecimal("1000.00"));

        when(walletRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(wallet));
    }

    @Test
    void get() {
        WalletDto dto = walletService.get(UUID.randomUUID());

        assertThat(dto).isNotNull()
                .hasFieldOrPropertyWithValue("id", wallet.getId())
                .hasFieldOrPropertyWithValue("balance", wallet.getBalance());
        verify(walletRepository, times(1)).findById(any(UUID.class));
        verify(walletConverter, times(1)).convertToDto(any(Wallet.class));
    }

    @Test
    void getThrowsWhenDoesNotExists() {
        when(walletRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrowsExactly(EntityNotFoundException.class, () -> walletService.get(UUID.randomUUID()));
        verify(walletRepository, times(1)).findById(any(UUID.class));
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    void makeTransaction(OperationType type, BigDecimal amount, BigDecimal expectedBalance) {
        when(walletRepository.save(any(Wallet.class)))
                .thenReturn(new Wallet(wallet.getId(), expectedBalance));
        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);

        WalletDto dto = walletService.makeTransaction(UUID.randomUUID(), type, amount);

        assertThat(dto).isNotNull()
                .hasFieldOrPropertyWithValue("balance", expectedBalance);
        verify(walletRepository).save(walletCaptor.capture()); // Capture the Wallet argument
        Wallet savedWallet = walletCaptor.getValue(); // Retrieve captured Wallet
        assertEquals(expectedBalance, savedWallet.getBalance()); // Verify the balance is correct
    }

    @Test
    void makeTransactionThrowsWhenWalletDoesNotExists() {
        when(walletRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrowsExactly(EntityNotFoundException.class,
                () -> walletService.makeTransaction(
                        UUID.randomUUID(),
                        OperationType.WITHDRAW,
                        new BigDecimal("0.0")));
    }

    @Test
    void makeTransactionThrowsWhenInsufficientFunds() {
        final String withdrawAmount = "1000.01";

        assertThrowsExactly(InsufficientFundsException.class,
                () -> walletService.makeTransaction(
                        UUID.randomUUID(),
                        OperationType.WITHDRAW,
                        new BigDecimal(withdrawAmount)));
    }

    /**
     * Provides a stream of arguments for testing the money operation.
     *
     * @return a stream of arguments, each representing a combination of operation type,
     * amount, and expected result. Each element in the stream is an instance of
     * {@link Arguments} containing:
     * <ul>
     * <li><b>OperationType</b> - the type of operation (e.g., {@link OperationType#WITHDRAW}
     * or {@link OperationType#DEPOSIT});</li>
     * <li><b>BigDecimal</b> amount - the amount involved in the operation;</li>
     * <li><b>Double</b> expected result - the expected result of the operation.</li>
     * </ul>
     */
    private static Stream<Arguments> getArguments() {
        return Stream.of(
                Arguments.of(OperationType.WITHDRAW, new BigDecimal("123.33"),
                        new BigDecimal("876.67")),
                Arguments.of(OperationType.WITHDRAW, new BigDecimal("989.89"),
                        new BigDecimal("10.11")),
                Arguments.of(OperationType.DEPOSIT, new BigDecimal("321.43"),
                        new BigDecimal("1321.43")),
                Arguments.of(OperationType.DEPOSIT, new BigDecimal("123456789012345678901234567890.12"),
                        new BigDecimal("123456789012345678901234568890.12"))
        );
    }
}