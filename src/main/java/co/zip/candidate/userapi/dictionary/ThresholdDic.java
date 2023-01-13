package co.zip.candidate.userapi.dictionary;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static co.zip.candidate.userapi.constant.CurrencyInstance.AUD;

@Component
public class ThresholdDic {
    private final Map<Currency, BigDecimal> thresholdMap = new HashMap<Currency, BigDecimal>(){
        {
            put(AUD, BigDecimal.valueOf(1000));
        }
    };

    public BigDecimal getCurrencyThreshold(Currency currency) {
        return thresholdMap.get(currency);
    }
}
