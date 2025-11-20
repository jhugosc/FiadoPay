package edu.ucsal.fiadopay.service;

import edu.ucsal.fiadopay.annotations.WebhookSink;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AsyncProcessorServiceTest {

    @Test
    void deveTerAnotacaoWebhookSinkCorreta() {
        Class<AsyncProcessorService> clazz = AsyncProcessorService.class;

        Assertions.assertTrue(clazz.isAnnotationPresent(WebhookSink.class), 
            "A classe AsyncProcessorService deveria ter a anotação @WebhookSink");

        WebhookSink anota = clazz.getAnnotation(WebhookSink.class);
        Assertions.assertTrue(anota.active(), "O WebhookSink deveria estar ativo");
        Assertions.assertNotNull(anota.description(), "A descrição não pode ser nula");
        
        System.out.println("Teste de Metadados: OK -> " + anota.description());
    }
}