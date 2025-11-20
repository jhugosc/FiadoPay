package edu.ucsal.fiadopay.service;

import org.springframework.stereotype.Service;

import edu.ucsal.fiadopay.annotations.WebhookSink;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jakarta.annotation.PreDestroy;

@Service
@WebhookSink(description = "Dispara notificações HTTP")
public class AsyncProcessorService {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public void submit(Runnable task) {
        executor.submit(task);
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }
}