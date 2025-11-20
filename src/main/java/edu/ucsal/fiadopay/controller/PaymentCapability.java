package edu.ucsal.fiadopay.controller;

public record PaymentCapability(
    String methodType, 
    boolean antiFraudActive, 
    double riskThreshold,
    String description
) {}