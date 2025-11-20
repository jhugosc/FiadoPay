package edu.ucsal.fiadopay.strategy;

import edu.ucsal.fiadopay.annotations.AntiFraud;
import edu.ucsal.fiadopay.annotations.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class CardStrategyTest {

    @Test
    void deveCalcularJurosCompostosCorretamente() {
        CardStrategy strategy = new CardStrategy();
        BigDecimal amount = new BigDecimal("100.00");
        
        BigDecimal total = strategy.calculateTotal(amount, 2);

        Assertions.assertEquals(new BigDecimal("102.01"), total, "O cálculo de juros compostos falhou.");
    }

    @Test
    void naoDeveCobrarJurosParaParcelaUnica() {
        CardStrategy strategy = new CardStrategy();
        BigDecimal amount = new BigDecimal("50.00");
        
        BigDecimal total = strategy.calculateTotal(amount, 1);

        Assertions.assertEquals(new BigDecimal("50.00"), total);
    }

    @Test
    void deveTerAnotacoesConfiguradas() {
        Class<CardStrategy> clazz = CardStrategy.class;
        
        Assertions.assertTrue(clazz.isAnnotationPresent(PaymentMethod.class), "A classe precisa de @PaymentMethod");
        Assertions.assertTrue(clazz.isAnnotationPresent(AntiFraud.class), "A classe precisa de @AntiFraud");
    }
}