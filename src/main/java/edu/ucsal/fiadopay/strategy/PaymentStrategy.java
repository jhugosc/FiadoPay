package edu.ucsal.fiadopay.strategy;

import java.math.BigDecimal;

public interface PaymentStrategy {
    BigDecimal calculateTotal(BigDecimal amount, Integer installments);
    Double getInterestRate();
}