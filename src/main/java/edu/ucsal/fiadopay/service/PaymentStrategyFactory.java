package edu.ucsal.fiadopay.service;

import edu.ucsal.fiadopay.annotations.PaymentMethod;
import edu.ucsal.fiadopay.annotations.WebhookSink;
import edu.ucsal.fiadopay.strategy.PaymentStrategy;

import org.reflections.Reflections;
import org.springframework.stereotype.Service;


import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class PaymentStrategyFactory {

    private final Map<String, PaymentStrategy> strategies = new HashMap<>();

    @PostConstruct
    public void init() {
        Reflections reflections = new Reflections("edu.ucsal.fiadopay");

        Set<Class<?>> strategyClasses = reflections.getTypesAnnotatedWith(PaymentMethod.class);
        for (Class<?> clazz : strategyClasses) {
            try {
                PaymentMethod annotation = clazz.getAnnotation(PaymentMethod.class);
                PaymentStrategy instance = (PaymentStrategy) clazz.getDeclaredConstructor().newInstance();
                strategies.put(annotation.type().toUpperCase(), instance);
                System.out.println("Strategies: Registrado via Reflexão: " + annotation.type());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Set<Class<?>> sinkClasses = reflections.getTypesAnnotatedWith(WebhookSink.class);
        for (Class<?> clazz : sinkClasses) {
            WebhookSink info = clazz.getAnnotation(WebhookSink.class);
            if (info != null && info.active()) {
                System.out.println("Infraestrutura: Webhook Sink detectado: " 
                    + clazz.getSimpleName() + " [" + info.description() + "]");
            }
        }
    }

    public PaymentStrategy getStrategy(String method) {
        return strategies.getOrDefault(method.toUpperCase(), new PaymentStrategy() {
             public BigDecimal calculateTotal(BigDecimal a, Integer i) { return a; }
             public Double getInterestRate() { return null; }
        });
    }

    public java.util.List<edu.ucsal.fiadopay.controller.PaymentCapability> getCapabilities() {
        var list = new java.util.ArrayList<edu.ucsal.fiadopay.controller.PaymentCapability>();

        for (var entry : strategies.entrySet()) {
            String type = entry.getKey();
            edu.ucsal.fiadopay.strategy.PaymentStrategy strategy = entry.getValue();
            Class<?> clazz = strategy.getClass(); 

            boolean hasAntiFraud = clazz.isAnnotationPresent(edu.ucsal.fiadopay.annotations.AntiFraud.class);
            double threshold = 0.0;
            
            if (hasAntiFraud) {
                threshold = clazz.getAnnotation(edu.ucsal.fiadopay.annotations.AntiFraud.class).riskThreshold();
            }
            
            list.add(new edu.ucsal.fiadopay.controller.PaymentCapability(
                type, 
                hasAntiFraud, 
                threshold,
                "Processador nativo para " + type
            ));
        }
        return list;
    }
}