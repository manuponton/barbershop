package com.empresa.barberia.api.v1;

import com.empresa.barberia.domain.Payment;

import java.time.LocalDateTime;

public record PaymentResponse(String id, String description, double amount, String method, String status, String authorization, LocalDateTime processedAt) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(payment.id(), payment.description(), payment.amount(), payment.method(), payment.status(), payment.authorization(), payment.processedAt());
    }
}
