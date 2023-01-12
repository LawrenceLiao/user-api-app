package co.zip.candidate.userapi.dto.client;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

@Value
@Builder
public class ClientGetDto {
    Long id;
    String clientName;
    String email;
    BigDecimal monthlySalary;
    BigDecimal monthlyExpenses;
    Currency currency;
}