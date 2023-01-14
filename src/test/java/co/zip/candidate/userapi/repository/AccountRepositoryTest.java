package co.zip.candidate.userapi.repository;

import co.zip.candidate.userapi.model.Account;
import co.zip.candidate.userapi.model.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static co.zip.candidate.userapi.constant.CurrencyInstance.AUD;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    private Account account;
    @BeforeEach
    void setUp() {
        client = Client.builder()
                .name("John Doe")
                .email("test@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(8000))
                .build();
        clientRepository.save(client);

        account = Account.builder()
                .accountNumber("test-AUD-0")
                .totalCredit(BigDecimal.valueOf(1000))
                .availableCredit(BigDecimal.valueOf(1000))
                .currency(AUD)
                .client(client)
                .build();
        accountRepository.save(account);
    }

    @Test
    void shouldHaveCreatedTimestampAndUpdatedTimestampAfterPersistedToDB() {
        assertNotNull(account.getCreatedAt());
        assertNotNull(account.getUpdatedAt());
    }
    @Test
    void shouldReturnAccountListWhenThereAreAccountsNotDeleted() {
        List<Account> accounts = accountRepository.findByDeletedIsFalse();

        assertFalse(accounts.isEmpty());
    }

    @Transactional
    @Test
    void shouldReturnEmptyListWhenThereIsNotAccountNotDeleted() {
        account.setDeleted(true);
        List<Account> accounts = accountRepository.findByDeletedIsFalse();

        assertTrue(accounts.isEmpty());

    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        clientRepository.deleteAll();
    }
}
