package co.zip.candidate.userapi.controller;


import co.zip.candidate.userapi.dto.account.AccountGetDto;
import co.zip.candidate.userapi.dto.account.AccountPostDto;
import co.zip.candidate.userapi.exception.InsufficientMonthlySurplusException;
import co.zip.candidate.userapi.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static co.zip.candidate.userapi.constant.CurrencyInstance.AUD;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    AccountService accountService;

    @Test
    void shouldReturnOkResponseWithAccountInfoWhenSuccessfullyCreatedOneAccount() throws Exception {
        AccountPostDto accountPostDto = AccountPostDto.builder()
                .clientId(1L)
                .build();

        AccountGetDto accountGetDto = AccountGetDto.builder()
                .accountNumber("1-AUD-0")
                .currency(AUD)
                .totalCredit(BigDecimal.valueOf(1000))
                .availableCredit(BigDecimal.valueOf(1000))
                .clientName("John Doe")
                .clientEmail("john.doe@test.com")
                .build();
        when(accountService.createAccount(accountPostDto)).thenReturn(accountGetDto);

        String json = objectMapper.writeValueAsString(accountPostDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/accounts")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value(accountGetDto.getAccountNumber()))
                .andExpect(jsonPath("$.clientName").value(accountGetDto.getClientName()))
                .andExpect(jsonPath("$.clientEmail").value(accountGetDto.getClientEmail()))
                .andExpect(jsonPath("$.totalCredit").value(accountGetDto.getTotalCredit()))
                .andExpect(jsonPath("$.availableCredit").value(accountGetDto.getAvailableCredit()));
    }

    @Test
    void shouldReturnPreconditionFailedResponseWhenInsufficientMonthlySurplusExceptionThrows() throws Exception {
        AccountPostDto accountPostDto = AccountPostDto.builder()
                .clientId(1L)
                .build();

        when(accountService.createAccount(accountPostDto)).thenThrow(InsufficientMonthlySurplusException.class);
        String json = objectMapper.writeValueAsString(accountPostDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/accounts")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.message").value("INSUFFICIENT_SURPLUS"));
    }

    @Test
    void shouldReturnServiceUnavailableFailedResponseWhenUnexpectedExceptionOccurred() throws Exception {
        AccountPostDto accountPostDto = AccountPostDto.builder()
                .clientId(1L)
                .build();

        when(accountService.createAccount(accountPostDto)).thenThrow(NullPointerException.class);
        String json = objectMapper.writeValueAsString(accountPostDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/accounts")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("ERROR_OCCURRED_IN_SERVER_SIDE"))
                .andExpect(jsonPath("$.details").value("Please contact admin"));
    }

    @Test
    void shouldReturnOkWhenGotAccountList() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/v1/accounts"))
                .andExpect(status().isOk()
                );
    }
}
