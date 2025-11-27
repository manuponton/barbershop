package com.empresa.barberia.api.v1;

public record PaymentPayload(String description, double amount, String method) {
}
