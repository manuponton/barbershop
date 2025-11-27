package com.empresa.barberiasaas.caja.infrastructure;

import com.empresa.barberiasaas.caja.domain.PaymentMethod;
import com.empresa.barberiasaas.caja.domain.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class FakePaymentGatewayClient implements PaymentGatewayClient {
    @Override
    public PaymentStatus process(String description, double amount, PaymentMethod method) {
        if (amount <= 0) {
            return PaymentStatus.DECLINED;
        }

        if (EnumSet.of(PaymentMethod.CASH, PaymentMethod.CARD, PaymentMethod.WALLET).contains(method)) {
            return PaymentStatus.APPROVED;
        }

        return PaymentStatus.DECLINED;
    }
}
