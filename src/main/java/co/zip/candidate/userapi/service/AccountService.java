package co.zip.candidate.userapi.service;

import co.zip.candidate.userapi.aop.MonthlySurplusValidation;
import co.zip.candidate.userapi.dictionary.ThresholdDic;
import co.zip.candidate.userapi.dto.account.AccountGetDto;
import co.zip.candidate.userapi.dto.account.AccountPostDto;
import co.zip.candidate.userapi.exception.DuplicateElementsException;
import co.zip.candidate.userapi.mapper.AccountMapper;
import co.zip.candidate.userapi.model.Account;
import co.zip.candidate.userapi.model.Client;
import co.zip.candidate.userapi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @MonthlySurplusValidation
    @Transactional
    public AccountGetDto createAccount(AccountPostDto accountPostDto) {
        Client client = clientService.retrieveClientById(accountPostDto.getClientId());
        validateIfAccountForCurrencyExists(client);
        Account account = createNewAccount(client, client.getCurrency());

        return accountMapper.fromEntity(account);
    }

    private Account createNewAccount(Client client, Currency currency) {
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
        return accountRepository.findByDeletedIsFalse().stream()
                .map(accountMapper::fromEntity)
                .collect(Collectors.toList());
    }

    private String generateAccountNumber(Client client) {
        String separator = "-";
        return StringUtils.join(List.of(client.getId(), client.getCurrency(), client.getAccounts().size()), separator);
    }

    // I assume there is only one active account for one currency with a client
    // e.g. A client only has one active account with AUD and has to do validation beforehand
    private void validateIfAccountForCurrencyExists(Client client) {
        client.getAccounts().stream()
                .filter(account -> isActiveDuplicateAccount(account, client.getCurrency()))
                .findFirst()
                .ifPresent(account -> {
                    log.info("Account for currency {} has already existed with client {}", client.getCurrency(), client.getId());
                    throw new DuplicateElementsException("Account for given currency has already been there with client");
                });
    }

    private boolean isActiveDuplicateAccount(Account account, Currency currency) {
        return !account.isDeleted() && account.getCurrency() == currency;
    }
}
