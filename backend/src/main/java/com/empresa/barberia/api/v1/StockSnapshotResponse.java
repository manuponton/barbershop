package com.empresa.barberia.api.v1;

public record StockSnapshotResponse(String productId, String name, int stock, double unitPrice, String category, boolean bajoMinimo) {
}
