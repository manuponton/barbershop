package com.empresa.barberia.domain;

import java.time.LocalDateTime;

public record CashSession(String id, LocalDateTime openedAt, LocalDateTime closedAt, double openingAmount, Double closingAmount) {
    public boolean isOpen() {
        return closedAt == null;
    }
}
