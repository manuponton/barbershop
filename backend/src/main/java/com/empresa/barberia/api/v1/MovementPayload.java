package com.empresa.barberia.api.v1;

public record MovementPayload(String productId, int quantity, double unitPrice, String note) {
}
