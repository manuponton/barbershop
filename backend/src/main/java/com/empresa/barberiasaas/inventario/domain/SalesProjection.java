package com.empresa.barberiasaas.inventario.domain;

public record SalesProjection(String productName, int quantitySold, double totalAmount) {
}
