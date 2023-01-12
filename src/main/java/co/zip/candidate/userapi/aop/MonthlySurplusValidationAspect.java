package co.zip.candidate.userapi.aop;

import co.zip.candidate.userapi.dictionary.ThresholdDic;
import co.zip.candidate.userapi.dto.account.AccountPostDto;
import co.zip.candidate.userapi.exception.InsufficientMonthlySurplusException;
import co.zip.candidate.userapi.model.Client;
import co.zip.candidate.userapi.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MonthlySurplusValidationAspect {
    private final ThresholdDic thresholdDic;

    private final ClientService clientService;

    @Around("@annotation(MonthlySurplusValidation)")
    public Object validateSurplus(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        AccountPostDto accountPostDto = (AccountPostDto) args[0];
        Client client = clientService.retrieveClientById(accountPostDto.getClientId());
        if(!isValid(client, client.getCurrency())) {
            log.info("Client: {} doesn't have enough monthly surplus to create an account", client.getId());
            throw new InsufficientMonthlySurplusException("Client's monthly surplus(salary - expenses) is insufficient");
        }
        return joinPoint.proceed();
    }

    private boolean isValid(Client client, Currency currency) {
        return client.getMonthlySalary().subtract(client.getMonthlyExpenses())
                .compareTo(thresholdDic.getCurrencyThreshold(currency)) > 0;
    }
}
