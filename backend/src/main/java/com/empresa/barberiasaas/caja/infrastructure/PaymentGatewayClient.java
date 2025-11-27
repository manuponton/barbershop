package com.empresa.barberiasaas.caja.infrastructure;

import com.empresa.barberiasaas.caja.domain.PaymentMethod;
import com.empresa.barberiasaas.caja.domain.PaymentStatus;

public interface PaymentGatewayClient {
    PaymentStatus process(String description, double amount, PaymentMethod method);
}
