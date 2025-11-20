package edu.ucsal.fiadopay.strategy;

import edu.ucsal.fiadopay.annotations.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class PixStrategyTest {

    @Test
    void pixNaoDeveTerJuros() {
        PixStrategy strategy = new PixStrategy();
        BigDecimal valorOriginal = new BigDecimal("150.00");

        BigDecimal total = strategy.calculateTotal(valorOriginal, 1);

        Assertions.assertEquals(valorOriginal, total, "O valor do PIX foi alterado indevidamente");
        Assertions.assertEquals(0.0, strategy.getInterestRate(), "A taxa de juros do PIX deve ser 0.0");
    }

    @Test
    void deveTerAnotacaoPixCorreta() {
        Class<PixStrategy> clazz = PixStrategy.class;

        Assertions.assertTrue(clazz.isAnnotationPresent(PaymentMethod.class), "Falta a anotação @PaymentMethod na classe PixStrategy");
        
        PaymentMethod anota = clazz.getAnnotation(PaymentMethod.class);
        Assertions.assertEquals("PIX", anota.type(), "O tipo da anotação deve ser 'PIX'");
    }
}