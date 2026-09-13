package com.example.lab9.controller;

import com.example.lab9.model.Account;
import com.example.lab9.model.DepositTransaction;
import com.example.lab9.repository.AccountRepository;
import com.example.lab9.repository.DepositRepository;
import com.example.lab9.service.DepositService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:lab9test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @SpyBean
    private DepositRepository depositRepository;

    @Autowired
    private DepositService depositService;

    @BeforeEach
    void clearTestDatabase() {
        reset(depositRepository);
        depositRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void createDepositAndReadAccount() throws Exception {
        String response = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"accountNumber":"1234567890","ownerName":"Test Owner","balance":0}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.balance").value(0.0))
                .andReturn().getResponse().getContentAsString();
        long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(post("/accounts/{id}/deposit", id)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"amount\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deposit successful"));

        mockMvc.perform(get("/accounts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerName").value("Test Owner"))
                .andExpect(jsonPath("$.balance").value(1000.0));
        assertThat(depositRepository.findAll()).singleElement().satisfies(deposit -> {
            assertThat(deposit.getAmount()).isEqualTo(1000.0);
            assertThat(deposit.getAccount().getId()).isEqualTo(id);
        });
    }

    @Test
    void rollbackBothWritesWhenErrorOccursAfterSavingDeposit() {
        Account account = createTestAccount();
        // Flush both writes before failing, to prove the database rolls them back.
        doAnswer(invocation -> {
            depositRepository.saveAndFlush(invocation.getArgument(0));
            throw new RuntimeException("Test Rollback");
        }).when(depositRepository).save(any(DepositTransaction.class));

        assertThatThrownBy(() -> depositService.deposit(account.getId(), 1000.0))
                .isInstanceOf(RuntimeException.class).hasMessage("Test Rollback");

        assertThat(accountRepository.findById(account.getId()).orElseThrow().getBalance())
                .isEqualTo(0.0);
        assertThat(depositRepository.count()).isZero();
    }

    @Test
    void rejectInvalidDepositWithoutChangingData() throws Exception {
        Account account = createTestAccount();
        for (String body : new String[]{"{}", "{\"amount\":0}", "{\"amount\":-100}"}) {
            mockMvc.perform(post("/accounts/{id}/deposit", account.getId())
                            .contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest());
        }
        assertThat(accountRepository.findById(account.getId()).orElseThrow().getBalance())
                .isEqualTo(0.0);
        assertThat(depositRepository.count()).isZero();
    }

    @Test
    void missingAccountReturnsNotFound() throws Exception {
        mockMvc.perform(get("/accounts/{id}", -1)).andExpect(status().isNotFound());
        mockMvc.perform(post("/accounts/{id}/deposit", -1)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"amount\":1000}"))
                .andExpect(status().isNotFound());
        assertThat(depositRepository.count()).isZero();
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setAccountNumber("test-account");
        account.setOwnerName("Test Owner");
        account.setBalance(0.0);
        return accountRepository.save(account);
    }
}
