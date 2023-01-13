package co.zip.candidate.userapi.aop;

import co.zip.candidate.userapi.dictionary.ThresholdDic;
import co.zip.candidate.userapi.dto.account.AccountPostDto;
import co.zip.candidate.userapi.exception.InsufficientMonthlySurplusException;
import co.zip.candidate.userapi.model.Client;
import co.zip.candidate.userapi.service.ClientService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("Test")
public class MonthlySurplusValidationAspectTest {

    static final Long CLIENT_ID = 1L;
    @MockBean
    ClientService clientService;

    @Autowired
    ThresholdDic thresholdDic;

    @Autowired
    MonthlySurplusValidationAspect monthlySurplusValidationAspect;

    @Test
    void shouldDoNothingWhenSurplusIsGood() throws Throwable {
        Client client = Client.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(8000))
                .build();

        when(clientService.retrieveClientById(CLIENT_ID)).thenReturn(client);

        ProceedingJoinPoint mockJoinPoint = mock(ProceedingJoinPoint.class);

        when(mockJoinPoint.getArgs()).thenReturn(new Object[] {new AccountPostDto(CLIENT_ID)});

        monthlySurplusValidationAspect.validateSurplus(mockJoinPoint);

        verify(mockJoinPoint).proceed();
    }

    @Test
    void shouldThrowInsufficientMonthlySurplusExceptionWhenSurplusIsNotEnough() throws Throwable {
        Client client = Client.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(9500))
                .build();

        when(clientService.retrieveClientById(CLIENT_ID)).thenReturn(client);

        ProceedingJoinPoint mockJoinPoint = mock(ProceedingJoinPoint.class);

        when(mockJoinPoint.getArgs()).thenReturn(new Object[] {new AccountPostDto(CLIENT_ID)});

        Assertions.assertThrows(InsufficientMonthlySurplusException.class,
                () -> monthlySurplusValidationAspect.validateSurplus(mockJoinPoint));

        verify(mockJoinPoint, never()).proceed();
    }
}
