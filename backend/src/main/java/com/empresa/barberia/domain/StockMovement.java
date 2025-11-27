package com.empresa.barberia.domain;

import java.time.LocalDateTime;

public record StockMovement(String id, String productId, int quantity, double unitPrice, String type, String note, LocalDateTime registeredAt) {
}
