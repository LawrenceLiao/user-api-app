package co.zip.candidate.userapi.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Currency;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CurrencyInstance {
    public static final Currency AUD = Currency.getInstance("AUD");
}
