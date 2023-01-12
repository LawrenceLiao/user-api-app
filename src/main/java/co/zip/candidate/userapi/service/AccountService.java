package co.zip.candidate.userapi.service;

import co.zip.candidate.userapi.dictionary.ThresholdDic;
import co.zip.candidate.userapi.dto.account.AccountGetDto;
import co.zip.candidate.userapi.dto.account.AccountPostDto;
import co.zip.candidate.userapi.exception.DuplicateElementsException;
import co.zip.candidate.userapi.exception.InsufficientMonthlySurplusException;
import co.zip.candidate.userapi.mapper.AccountMapper;
import co.zip.candidate.userapi.model.Account;
import co.zip.candidate.userapi.model.Client;
import co.zip.candidate.userapi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final ClientService clientService;
    private final AccountMapper accountMapper;
    private final ThresholdDic thresholdDic;

    public AccountGetDto createAccount(AccountPostDto accountPostDto) {
        Client client = clientService.retrieveClientById(accountPostDto.getClientId());

        validateForCredit(client);
        validateIfAccountForCurrencyExists(client);
        Account account = createAccount(client, client.getCurrency());

        return accountMapper.fromEntity(account);
    }

    @Transactional
    public Account createAccount(Client client, Currency currency) {
        BigDecimal threshold = thresholdDic.getCurrencyThreshold(currency);
        Account account = Account.builder()
                .accountNumber(generateAccountNumber(client))
                .currency(client.getCurrency())
                .totalCredit(threshold)
                .availableCredit(threshold)
                .client(client)
                .build();
        accountRepository.save(account);
        return account;
    }

    public List<AccountGetDto> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::fromEntity)
                .collect(Collectors.toList());
    }

    private String generateAccountNumber(Client client) {
        String separator = "-";
        return StringUtils.join(List.of(client.getId(), client.getCurrency(), client.getAccounts().size()), separator);
    }

    private void validateForCredit(Client client) {
        if (client.getMonthlySalary().subtract(client.getMonthlyExpenses())
                .compareTo(thresholdDic.getCurrencyThreshold(client.getCurrency())) < 0) {
            log.info("Client: {} doesn't have enough monthly surplus to create an account", client.getId());
            throw new InsufficientMonthlySurplusException("Client's monthly surplus(salary - expenses) is insufficient");
        }
    }

    // I assume there is only one account for one currency with a client
    // e.g. A client only has one account with AUD and has to do validation beforehand
    private void validateIfAccountForCurrencyExists(Client client) {
        client.getAccounts().stream()
                .filter(account -> account.getCurrency() == client.getCurrency())
                .findFirst()
                .ifPresent(account -> {
                    log.info("Account for currency {} has already existed with client {}", client.getCurrency(), client.getId());
                    throw new DuplicateElementsException("Account for given currency has already been there with client");
                });
    }
}
