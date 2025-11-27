package com.empresa.barberiasaas.inventario.domain;

import java.util.UUID;

public record StockSnapshot(UUID productId, String name, int stock, double unitPrice, String category) {
}
