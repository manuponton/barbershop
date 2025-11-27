package com.empresa.barberiasaas.inventario.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {

    @Test
    void preventsNegativeStock() {
        var item = new InventoryItem(UUID.randomUUID(), "Cera", 2, 1);
        assertThrows(IllegalStateException.class, () -> item.decrease(3));
    }

    @Test
    void flagsLowStockWhenUnderMinimum() {
        var item = new InventoryItem(UUID.randomUUID(), "Shampoo", 2, 3);
        assertTrue(item.hasLowStock());
    }
}
