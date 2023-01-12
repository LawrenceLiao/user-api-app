package co.zip.candidate.userapi.repository;

import co.zip.candidate.userapi.model.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@SpringBootTest
@ActiveProfiles("test")
public class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;


    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    void shouldReturnTrueWhenEmailGivenIsAlreadyPersistedWithAnotherClient() {
        String email = "test@test.com";
        Client client = Client.builder()
                .name("John Doe")
                .email(email)
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(8000))
                .build();
        clientRepository.save(client);
        Assertions.assertTrue(clientRepository.existsByEmail(email));
    }
}
