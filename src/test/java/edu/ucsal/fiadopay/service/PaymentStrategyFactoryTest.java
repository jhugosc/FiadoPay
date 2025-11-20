package edu.ucsal.fiadopay.service;

import edu.ucsal.fiadopay.strategy.BoletoStrategy;
import edu.ucsal.fiadopay.strategy.CardStrategy;
import edu.ucsal.fiadopay.strategy.DebitStrategy;
import edu.ucsal.fiadopay.strategy.PaymentStrategy;
import edu.ucsal.fiadopay.strategy.PixStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentStrategyFactoryTest {

    private PaymentStrategyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PaymentStrategyFactory();
        factory.init();
    }

    @Test
    void deveDescobrirEstrategiaPixSozinho() {
        PaymentStrategy strategy = factory.getStrategy("PIX");
        
        Assertions.assertNotNull(strategy, "A Factory não encontrou a estratégia PIX.");
        Assertions.assertTrue(strategy instanceof PixStrategy, "A classe retornada não é PixStrategy.");
    }

    @Test
    void deveDescobrirEstrategiaCardSozinho() {
        PaymentStrategy strategy = factory.getStrategy("CARD");
        
        Assertions.assertNotNull(strategy, "A Factory não encontrou a estratégia CARD.");
        Assertions.assertTrue(strategy instanceof CardStrategy, "A classe retornada não é CardStrategy.");
    }

    @Test
    void deveDescobrirEstrategiaBoletoSozinho() {
        PaymentStrategy strategy = factory.getStrategy("BOLETO");
        
        Assertions.assertNotNull(strategy, "A Factory deveria ter achado o Boleto via reflexão");
        Assertions.assertTrue(strategy instanceof BoletoStrategy);
    }

    @Test
    void deveDescobrirEstrategiaDebitSozinho() {
        PaymentStrategy strategy = factory.getStrategy("DEBIT");
        
        Assertions.assertNotNull(strategy, "A Factory deveria ter achado o Debit via reflexão");
        Assertions.assertTrue(strategy instanceof DebitStrategy);
    }

    @Test
    void deveEncontrarWebhookSinksNoPacoteRaiz() {
        org.reflections.Reflections reflections = new org.reflections.Reflections("edu.ucsal.fiadopay");
        
        java.util.Set<Class<?>> sinks = reflections.getTypesAnnotatedWith(edu.ucsal.fiadopay.annotations.WebhookSink.class);

        Assertions.assertFalse(sinks.isEmpty(), "Deveria ter encontrado pelo menos um WebhookSink");
        Assertions.assertTrue(sinks.contains(AsyncProcessorService.class), 
            "A varredura deveria ter encontrado o AsyncProcessorService");
            
        System.out.println("Teste de Scan: Encontrou " + sinks.size() + " classes anotadas.");
    }

    @Test
    void deveListarCapacidadesComMetadadosCorretos() {
        var capabilities = factory.getCapabilities();

        Assertions.assertFalse(capabilities.isEmpty(), "A lista de capacidades não pode estar vazia.");
        
        var cardCap = capabilities.stream()
                .filter(c -> c.methodType().equals("CARD"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Deveria ter encontrado a capacidade CARD"));

        Assertions.assertTrue(cardCap.antiFraudActive(), "O método CARD deveria estar com AntiFraud ativo.");
        Assertions.assertEquals(0.7, cardCap.riskThreshold(), "O limite de risco do CARD deveria ser 0.7 (valor da anotação).");

        var pixCap = capabilities.stream()
                .filter(c -> c.methodType().equals("PIX"))
                .findFirst()
                .orElseThrow();
        
        Assertions.assertFalse(pixCap.antiFraudActive(), "O método PIX não deveria ter AntiFraud.");
    }
}