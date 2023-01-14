package co.zip.candidate.userapi.service;


import co.zip.candidate.userapi.dictionary.ThresholdDic;
import co.zip.candidate.userapi.dto.account.AccountGetDto;
import co.zip.candidate.userapi.dto.account.AccountPostDto;
import co.zip.candidate.userapi.exception.DuplicateElementsException;
import co.zip.candidate.userapi.mapper.AccountMapper;
import co.zip.candidate.userapi.mapper.AccountMapperImpl;
import co.zip.candidate.userapi.model.Account;
import co.zip.candidate.userapi.model.Client;
import co.zip.candidate.userapi.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static co.zip.candidate.userapi.constant.CurrencyInstance.AUD;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    private static final Long CLIENT_ID = 1L;
    @Mock
    private ClientService clientService;
    @Mock
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private ThresholdDic thresholdDic;
    private AccountService accountService;

    private AccountPostDto accountPostDto;

    private Client client;

    @BeforeEach
    void setUp() {
        accountMapper = new AccountMapperImpl();
        thresholdDic = new ThresholdDic();

        accountService = new AccountService(accountRepository, clientService, accountMapper, thresholdDic);
        accountPostDto = AccountPostDto.builder()
                .clientId(CLIENT_ID)
                .build();
        client = Client.builder()
                .id(CLIENT_ID)
                .name("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(8000))
                .build();
    }

    @Test
    void shouldReturnAccountGetDtoWhenSuccessfullyCreatedAccount() {

        AccountGetDto expectedDto = AccountGetDto.builder()
                .accountNumber("1-AUD-0")
                .currency(AUD)
                .totalCredit(BigDecimal.valueOf(1000))
                .availableCredit(BigDecimal.valueOf(1000))
                .clientName(client.getName())
                .clientEmail(client.getEmail())
                .build();

        when(clientService.retrieveClientById(CLIENT_ID)).thenReturn(client);

        AccountGetDto accountGetDto = accountService.createAccount(accountPostDto);
        verify(accountRepository).save(any());
        Assertions.assertEquals(expectedDto, accountGetDto);

    }

    @Test
    void shouldThrowDuplicateElementExceptionWhenTheClientAlreadyHasAccountWithCurrencyGiven() {
        Account account = Account.builder()
                .accountNumber("1-AUD-0")
                .currency(AUD)
                .totalCredit(BigDecimal.valueOf(1000))
                .availableCredit(BigDecimal.valueOf(1000))
                .build();

        Client clientWithAccounts = client.toBuilder()
                .accounts(List.of(account))
                .build();

        when(clientService.retrieveClientById(CLIENT_ID)).thenReturn(clientWithAccounts);

        Assertions.assertThrows(DuplicateElementsException.class, () -> accountService.createAccount(accountPostDto));
    }

    @Test
    void shouldReturnAccountGetDtoListWhenRetrievingAllAccounts() {
        Client elon = Client.builder()
                .id(2L)
                .name("Elon Mush")
                .email("elon.musk@test.com")
                .monthlySalary(BigDecimal.valueOf(80000))
                .monthlyExpenses(BigDecimal.valueOf(74500))
                .build();

        List<Account> accounts = List.of(
                Account.builder()
                        .accountNumber("1-AUD-0")
                        .currency(AUD)
                        .totalCredit(BigDecimal.valueOf(1000))
                        .availableCredit(BigDecimal.valueOf(1000))
                        .client(client)
                        .build(),
                Account.builder()
                        .accountNumber("2-AUD-0")
                        .currency(AUD)
                        .totalCredit(BigDecimal.valueOf(1000))
                        .availableCredit(BigDecimal.valueOf(1000))
                        .client(elon)
                        .build()
        );

        when(accountRepository.findByDeletedIsFalse()).thenReturn(accounts);

        List<AccountGetDto> dtos = accountService.getAllAccounts();

        List<AccountGetDto> expectedDtos = List.of(
                AccountGetDto.builder()
                        .accountNumber("1-AUD-0")
                        .currency(AUD)
                        .totalCredit(BigDecimal.valueOf(1000))
                        .availableCredit(BigDecimal.valueOf(1000))
                        .clientName(client.getName())
                        .clientEmail(client.getEmail())
                        .build(),
                AccountGetDto.builder()
                        .accountNumber("2-AUD-0")
                        .currency(AUD)
                        .totalCredit(BigDecimal.valueOf(1000))
                        .availableCredit(BigDecimal.valueOf(1000))
                        .clientName(elon.getName())
                        .clientEmail(elon.getEmail())
                        .build()
        );

        Assertions.assertEquals(expectedDtos, dtos);
    }

}
