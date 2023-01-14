package co.zip.candidate.userapi.repository;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class ClientRepositoryTest {

    private static final String EMAIL = "test@test.com";
    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .name("John Doe")
                .email(EMAIL)
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(8000))
                .deleted(false)
                .build();
        clientRepository.save(client);
    }

    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    void shouldReturnTrueWhenEmailGivenIsAlreadyPersistedWithAnotherClient() {

        assertNotNull(client.getCreatedAt());
        assertNotNull(client.getUpdatedAt());

        assertTrue(clientRepository.existsByEmailAndDeletedIsFalse(EMAIL));
    }

    @Test
    void shouldReturnFalseWhenEmailGivenDoesNotExist() {
        String wrongEmail = "wrong@test.com";
        assertFalse(clientRepository.existsByEmailAndDeletedIsFalse(wrongEmail));
    }

    @Transactional
    @Test
    void shouldReturnFalseWhenEmailGivenExistsButClientIsDeleted() {
        client.setDeleted(true);
        assertFalse(clientRepository.existsByEmailAndDeletedIsFalse(EMAIL));
    }

    @Test
    void shouldReturnClientWhenOfferingCorrectIdAndIsNotDeleted() {
        Optional<Client> optionalClient = clientRepository.findByIdAndDeletedIsFalse(client.getId());

        assertTrue(optionalClient.isPresent());
    }

    @Transactional
    @Test
    void shouldReturnEmptyOptionalWhenOfferCorrectIdAndIsDeleted() {
        client.setDeleted(true);
        Optional<Client> optionalClient = clientRepository.findByIdAndDeletedIsFalse(client.getId());

        assertFalse(optionalClient.isPresent());
    }

    @Test
    void shouldReturnClientListWhenThereAreClientsNotDeleted() {
        List<Client> clients = clientRepository.findByDeletedIsFalse();

        assertFalse(clients.isEmpty());
    }

    @Transactional
    @Test
    void shouldReturnEmptyListWhenThereIsNoClientNotDeleted() {
        client.setDeleted(true);
        List<Client> clients = clientRepository.findByDeletedIsFalse();

        assertTrue(clients.isEmpty());
    }

}
