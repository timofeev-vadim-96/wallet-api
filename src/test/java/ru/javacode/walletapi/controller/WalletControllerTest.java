package ru.javacode.walletapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javacode.walletapi.dto.TransactionDto;
import ru.javacode.walletapi.dto.WalletDto;
import ru.javacode.walletapi.service.WalletService;
import ru.javacode.walletapi.util.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {WalletController.class})
class WalletControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalletService walletService;

    @Test
    void post() throws Exception {
        TransactionDto transaction = new TransactionDto(
                UUID.randomUUID(),
                OperationType.DEPOSIT,
                new BigDecimal("111.11"));
        WalletDto dto = new WalletDto(transaction.getWalletId(), new BigDecimal("888.89"));
        when(walletService.makeTransaction(
                transaction.getWalletId(),
                transaction.getOperationType(),
                transaction.getAmount()))
                .thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void get() throws Exception {
        WalletDto dto = new WalletDto(UUID.randomUUID(), new BigDecimal("1000.0"));
        when(walletService.get(dto.getId()))
                .thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/wallet/{id}", dto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }
}