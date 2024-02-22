package com.example.bankingservice.controller;

import com.example.bankingservice.BaseIntegrationTest;
import com.example.bankingservice.dto.AccountDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerIT extends BaseIntegrationTest {
    @Test
    public void unauthenticatedRequestThrows401() throws Exception {
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("admin@email.com")
    public void authenticatedUserCanSeeOnlyHisOwnAccounts() throws Exception {
        List<AccountDTO> expected = List.of(
                new AccountDTO(1L, "GB33BUKB20201555555555", "REGULAR", "0.00", "EUR"),
                new AccountDTO(2L, "GB33BUKB20201555555556", "SAVINGS","0.00", "EUR")
        );

        ResultActions resultActions = mockMvc.perform(get("/api/accounts")).andExpect(status().isOk());
        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        List<AccountDTO> response = objectMapper.readValue(content, new TypeReference<>() {});

        assertThat(response.size()).isEqualTo(2);
        assertThat(response).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @WithUserDetails("admin@email.com")
    public void authenticatedUserCanNotSeeOthersAccounts() throws Exception {
        mockMvc.perform(get("/api/accounts/3")).andExpect(status().isNotFound());
    }
}