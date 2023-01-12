package co.zip.candidate.userapi.dto.account;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

@Builder
@Value
public class AccountGetDto {
    Long id;
    String accountNumber;
    Currency currency;
    BigDecimal totalCredit;
    BigDecimal availableCredit;
    String clientName;
    String clientEmail;
}
