package com.empresa.barberiasaas.inventario.infrastructure;

public record ProductRequest(String name, String category, double price, int stock) {
}
