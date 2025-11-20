package edu.ucsal.fiadopay.strategy;

import edu.ucsal.fiadopay.annotations.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class DebitStrategyTest {

    @Test
    void debitoNaoDeveTerJuros() {
        DebitStrategy strategy = new DebitStrategy();
        BigDecimal valor = new BigDecimal("200.00");

        BigDecimal total = strategy.calculateTotal(valor, 1);

        Assertions.assertEquals(valor, total);
        Assertions.assertEquals(0.0, strategy.getInterestRate());
    }

    @Test
    void deveTerAnotacaoDebit() {
        Assertions.assertTrue(DebitStrategy.class.isAnnotationPresent(PaymentMethod.class));
        Assertions.assertEquals("DEBIT", DebitStrategy.class.getAnnotation(PaymentMethod.class).type());
    }
}