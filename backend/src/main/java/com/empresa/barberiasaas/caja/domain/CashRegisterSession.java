package com.empresa.barberiasaas.caja.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record CashRegisterSession(UUID id, LocalDateTime openedAt, LocalDateTime closedAt, double openingAmount, Double closingAmount) {
    public boolean isOpen() {
        return closedAt == null;
    }
}
