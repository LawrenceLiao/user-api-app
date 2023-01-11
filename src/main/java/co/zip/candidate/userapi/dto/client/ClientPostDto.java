package co.zip.candidate.userapi.dto.client;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Value
@Builder
public class ClientPostDto {
    @NotBlank(message = "Client name is not presented")
    String clientName;
    @NotBlank(message = "Email address is not presented")
    @Email(message = "Email address is invalid")
    String email;

    @NotNull(message = "Monthly salary has to be entered")
    @Positive(message = "Monthly salary has to be positive")
    BigDecimal monthlySalary;

    @NotNull(message = "Monthly expenses have to be entered")
    @Positive(message = "Monthly expenses has to be positive")
    BigDecimal monthlyExpenses;
}