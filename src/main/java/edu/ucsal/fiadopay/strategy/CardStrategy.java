package edu.ucsal.fiadopay.strategy;

import edu.ucsal.fiadopay.annotations.AntiFraud;
import edu.ucsal.fiadopay.annotations.PaymentMethod;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@PaymentMethod(type = "CARD")
@AntiFraud
public class CardStrategy implements PaymentStrategy {

    @Override
    public BigDecimal calculateTotal(BigDecimal amount, Integer installments) {
        if (installments != null && installments > 1) {
            BigDecimal base = new BigDecimal("1.01");
            BigDecimal factor = base.pow(installments);
            return amount.multiply(factor).setScale(2, RoundingMode.HALF_UP);
        }
        return amount;
    }

    @Override
    public Double getInterestRate() {
        return 1.0;
    }
}