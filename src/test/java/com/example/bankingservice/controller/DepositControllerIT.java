package com.example.bankingservice.controller;

import com.example.bankingservice.BaseIntegrationTest;
import com.example.bankingservice.domain.LedgerRecord;
import com.example.bankingservice.domain.OperationType;
import com.example.bankingservice.domain.Transaction;
import com.example.bankingservice.dto.AmountDTO;
import com.example.bankingservice.repository.LedgerRecordRepository;
import com.example.bankingservice.repository.TransactionHistoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.bankingservice.domain.TransactionType.Cr;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DepositControllerIT extends BaseIntegrationTest {
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    LedgerRecordRepository ledgerRecordRepository;

    @Test
    @WithUserDetails("user@email.com")
    void depositToAccountYouDoNotOwnFails() throws Exception {
        AmountDTO amountDTO = new AmountDTO(new BigDecimal("50.50"));

        String requestBody = objectMapper.writeValueAsString(amountDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/deposits/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("user@email.com")
    void depositToOwnAccountWorks() throws Exception {
        AmountDTO amountDTO = new AmountDTO(new BigDecimal("50.50"));

        String requestBody = objectMapper.writeValueAsString(amountDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/deposits/3")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        String response = objectMapper.readValue(content, new TypeReference<>() {});

        Optional<Transaction> transactionByExternalId = transactionHistoryRepository.findTransactionByExternalId(UUID.fromString(response));
        assertThat(transactionByExternalId).isPresent();
        Transaction transaction = transactionByExternalId.get();
        assertThat(transaction.getAmount()).isEqualTo(new BigDecimal("50.50"));
        assertThat(transaction.getOperationType()).isEqualTo(OperationType.DEPOSIT);

        List<LedgerRecord> all = (List<LedgerRecord>) ledgerRecordRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getTransactionType()).isEqualTo(Cr);
        assertThat(all.get(0).getAmount()).isEqualTo(5050L);
        assertThat(all.get(0).getTransactionId()).isEqualTo(transaction.getId());
    }

    @Test
    @WithUserDetails("user@email.com")
    void badRequestIsReturnedWithListOfErrorsWhenAmountIsNegative() throws Exception {
        AmountDTO amountDTO = new AmountDTO(new BigDecimal("-100"));
        String requestBody = objectMapper.writeValueAsString(amountDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/deposits/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<>() {};
        HashMap<String, Object> response = objectMapper.readValue(content, typeRef);

        assertThat(response).extracting("errors").asList()
                .containsAnyElementsOf(List.of("must be greater than 1.0", "Amount should be positive"));
    }

    @Test
    @WithUserDetails("user@email.com")
    void depositToNonExistingAccountFails() throws Exception {
        AmountDTO amountDTO = new AmountDTO(new BigDecimal("10.00"));
        String requestBody = objectMapper.writeValueAsString(amountDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/deposits/100")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}