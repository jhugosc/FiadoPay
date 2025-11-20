package edu.ucsal.fiadopay.controller;

import edu.ucsal.fiadopay.service.PaymentStrategyFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class CapabilitiesController {

    private final PaymentStrategyFactory factory;

    public CapabilitiesController(PaymentStrategyFactory factory) {
        this.factory = factory;
    }

    @GetMapping("/fiadopay/capabilities")
    public List<PaymentCapability> getCapabilities() {
        return factory.getCapabilities();
    }
}