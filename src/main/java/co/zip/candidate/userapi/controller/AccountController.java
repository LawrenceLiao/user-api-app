package co.zip.candidate.userapi.controller;

import co.zip.candidate.userapi.dto.account.AccountGetDto;
import co.zip.candidate.userapi.dto.account.AccountPostDto;
import co.zip.candidate.userapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/accounts")
public class AccountController {
    private final AccountService accountService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountGetDto createAccount(@Valid @RequestBody AccountPostDto accountPostDto) {
        return accountService.createAccount(accountPostDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountGetDto> getAllAccounts() {
        return accountService.getAllAccounts();
    }
}
