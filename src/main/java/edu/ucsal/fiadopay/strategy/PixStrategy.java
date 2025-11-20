package edu.ucsal.fiadopay.strategy;

import edu.ucsal.fiadopay.annotations.PaymentMethod;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@PaymentMethod(type = "PIX")
public class PixStrategy implements PaymentStrategy {

    @Override
    public BigDecimal calculateTotal(BigDecimal amount, Integer installments) {
        return amount;
    }

    @Override
    public Double getInterestRate() {
        return 0.0;
    }
}