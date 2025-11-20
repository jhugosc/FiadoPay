package edu.ucsal.fiadopay.strategy;

import edu.ucsal.fiadopay.annotations.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class BoletoStrategyTest {

    @Test
    void boletoNaoDeveTerJurosNemParcelamento() {
        BoletoStrategy strategy = new BoletoStrategy();
        
        BigDecimal total = strategy.calculateTotal(new BigDecimal("100.00"), 10);
        
        Assertions.assertEquals(new BigDecimal("100.00"), total);
        Assertions.assertEquals(0.0, strategy.getInterestRate());
    }

    @Test
    void deveTerAnotacaoBoleto() {
        Assertions.assertTrue(BoletoStrategy.class.isAnnotationPresent(PaymentMethod.class));
        Assertions.assertEquals("BOLETO", BoletoStrategy.class.getAnnotation(PaymentMethod.class).type());
    }
}