package com.empresa.barberiasaas.inventario.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovement(UUID id, UUID productId, MovementType type, int quantity, double unitPrice, LocalDateTime occurredAt, String note) {
}
