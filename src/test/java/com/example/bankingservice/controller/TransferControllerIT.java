package com.example.bankingservice.controller;

import com.example.bankingservice.BaseIntegrationTest;
import com.example.bankingservice.domain.Account;
import com.example.bankingservice.dto.AmountDTO;
import com.example.bankingservice.dto.TransferDTO;
import com.example.bankingservice.repository.LedgerRecordRepository;
import com.example.bankingservice.repository.TransactionHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class TransferControllerIT extends BaseIntegrationTest {
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    LedgerRecordRepository ledgerRecordRepository;

    @Test
    @WithUserDetails("admin@email.com")
    public void userCanTransferBetweenOwnAccountsRegularToSavings() throws Exception {
        AmountDTO amountDTO = new AmountDTO(new BigDecimal("70.00"));
        String requestBody = objectMapper.writeValueAsString(amountDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/deposits/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        TransferDTO transferDTO = new TransferDTO(1L, 2L, new BigDecimal("70.00"));
        String transferRequestBody = objectMapper.writeValueAsString(transferDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/transfers")
                        .content(transferRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Account> byOwnerId = accountRepository.findByOwnerId(1L);
        assertThat(byOwnerId).hasSize(2);
        assertThat(byOwnerId.get(0).getBalance()).isEqualTo(0L);
        assertThat(byOwnerId.get(1).getBalance()).isEqualTo(7000L);
    }

    @Test
    @WithUserDetails("admin@email.com")
    public void userCanTransferBetweenOwnAccountsSavingToRegular() throws Exception {
        AmountDTO amountDTO = new AmountDTO(new BigDecimal("50.00"));
        String requestBody = objectMapper.writeValueAsString(amountDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/deposits/2")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        TransferDTO transferDTO = new TransferDTO(2L, 1L, new BigDecimal("50.00"));
        String transferRequestBody = objectMapper.writeValueAsString(transferDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/transfers")
                        .content(transferRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        Optional<Account> account1 = accountRepository.findById(1L);
        assertThat(account1).isPresent();
        assertThat(account1.get().getBalance()).isEqualTo(0L);
        Optional<Account> account2 = accountRepository.findById(2L);
        assertThat(account2).isPresent();
        assertThat(account2.get().getBalance()).isEqualTo(5000L);
    }

}