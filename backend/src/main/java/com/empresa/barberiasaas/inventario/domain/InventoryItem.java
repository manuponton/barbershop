package com.empresa.barberiasaas.inventario.domain;

import java.util.Objects;
import java.util.UUID;

public record InventoryItem(UUID id, String name, int stock, int minimumStock) {

    public InventoryItem {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (minimumStock < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }
    }

    public InventoryItem increase(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        return new InventoryItem(id, name, stock + quantity, minimumStock);
    }

    public InventoryItem decrease(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (stock - quantity < 0) {
            throw new IllegalStateException("No se permite stock negativo");
        }
        return new InventoryItem(id, name, stock - quantity, minimumStock);
    }

    public boolean hasLowStock() {
        return stock <= minimumStock;
    }
}
