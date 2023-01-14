package co.zip.candidate.userapi.service;

import co.zip.candidate.userapi.dto.client.ClientGetDto;
import co.zip.candidate.userapi.dto.client.ClientPostDto;
import co.zip.candidate.userapi.exception.DuplicateElementsException;
import co.zip.candidate.userapi.exception.UserNotFoundException;
import co.zip.candidate.userapi.mapper.ClientMapper;
import co.zip.candidate.userapi.mapper.ClientMapperImpl;
import co.zip.candidate.userapi.model.Client;
import co.zip.candidate.userapi.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static co.zip.candidate.userapi.constant.CurrencyInstance.AUD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    private ClientService clientService;

    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
        clientMapper = new ClientMapperImpl();
        clientService = new ClientService(clientRepository, clientMapper);
    }

    @Test
    void shouldReturnClientGetDtoWhenSuccessfullyCreateAClient() {
        ClientPostDto clientPostDto = mockSinglePostDto();

        when(clientRepository.existsByEmail(eq(clientPostDto.getEmail()))).thenReturn(false);

        ClientGetDto clientGetDto = clientService.createClient(clientPostDto);

        verify(clientRepository).save(any());

        ClientGetDto expectedGetDto = ClientGetDto.builder()
                .clientName("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(8000))
                .currency(AUD)
                .build();

        assertEquals(expectedGetDto, clientGetDto);
    }

    @Test
    void shouldThrowDuplicateElementExceptionWhenEmailAlreadyExisted() {
        ClientPostDto clientPostDto = mockSinglePostDto();

        when(clientRepository.existsByEmail(eq(clientPostDto.getEmail()))).thenReturn(true);

        assertThrows(DuplicateElementsException.class, () -> clientService.createClient(clientPostDto));
    }

    @Test
    void shouldReturnClientGetDtoWhenSuccessfullyGotClientFromDatabase() {
        Long clientId = 1L;

        when(clientRepository.findByIdAndDeletedIsFalse(clientId)).thenReturn(Optional.of(mockSingleClient()));

        ClientGetDto clientGetDto = clientService.getClient(clientId);

        ClientGetDto expectedGetDto = ClientGetDto.builder()
                .id(1L)
                .clientName("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(8000))
                .currency(AUD)
                .build();

        assertEquals(expectedGetDto, clientGetDto);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenThereIsNoClientWithIdGiven() {
        Long clientId = 1L;

        when(clientRepository.findByIdAndDeletedIsFalse(clientId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> clientService.getClient(clientId));
    }

    @Test
    void shouldReturnClientGetDtoListWhenRetrievingAllClients() {
        when(clientRepository.findByDeletedIsFalse()).thenReturn(mockClientList());

        List<ClientGetDto> dtoList = clientService.getAllClients();

        List<ClientGetDto> expectedDtoList = List.of(
                ClientGetDto.builder()
                        .id(1L)
                        .clientName("John Doe")
                        .email("john.doe@test.com")
                        .monthlySalary(BigDecimal.valueOf(10000))
                        .monthlyExpenses(BigDecimal.valueOf(8000))
                        .currency(AUD)
                        .build(),
                ClientGetDto.builder()
                        .id(2L)
                        .clientName("Elon Mush")
                        .email("elon.musk@test.com")
                        .monthlySalary(BigDecimal.valueOf(80000))
                        .monthlyExpenses(BigDecimal.valueOf(74500))
                        .currency(AUD)
                        .build()
        );

        assertEquals(expectedDtoList, dtoList);
    }

    private Client mockSingleClient() {
        return Client.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(8000))
                .build();
    }

    private ClientPostDto mockSinglePostDto() {
        return ClientPostDto.builder()
                .clientName("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(8000))
                .build();
    }

    private List<Client> mockClientList() {
        return List.of(
                Client.builder()
                        .id(1L)
                        .name("John Doe")
                        .email("john.doe@test.com")
                        .monthlySalary(BigDecimal.valueOf(10000))
                        .monthlyExpenses(BigDecimal.valueOf(8000))
                        .build(),
                Client.builder()
                        .id(2L)
                        .name("Elon Mush")
                        .email("elon.musk@test.com")
                        .monthlySalary(BigDecimal.valueOf(80000))
                        .monthlyExpenses(BigDecimal.valueOf(74500))
                        .build()
        );
    }


}
