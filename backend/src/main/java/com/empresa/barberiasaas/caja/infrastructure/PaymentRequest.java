package com.empresa.barberiasaas.caja.infrastructure;

import com.empresa.barberiasaas.caja.domain.PaymentMethod;

public record PaymentRequest(String description, double amount, PaymentMethod method) {
}
