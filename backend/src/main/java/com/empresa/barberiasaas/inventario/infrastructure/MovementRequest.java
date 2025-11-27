package com.empresa.barberiasaas.inventario.infrastructure;

import java.util.UUID;

public record MovementRequest(UUID productId, int quantity, double unitPrice, String note) {
}
