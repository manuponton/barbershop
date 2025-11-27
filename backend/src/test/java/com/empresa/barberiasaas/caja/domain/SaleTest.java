package com.empresa.barberiasaas.caja.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaleTest {

    @Test
    void calculatesTotalWithMultipleItems() {
        var sale = new Sale(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), List.of(
                new SaleItem(UUID.randomUUID(), "Corte", SaleItem.ItemType.SERVICE, BigDecimal.valueOf(10), 1),
                new SaleItem(UUID.randomUUID(), "Cera", SaleItem.ItemType.PRODUCT, BigDecimal.valueOf(5), 2)
        ));

        assertEquals(BigDecimal.valueOf(20), sale.total());
    }

    @Test
    void calculatesCommissionForBarber() {
        var sale = new Sale(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), List.of(
                new SaleItem(UUID.randomUUID(), "Corte", SaleItem.ItemType.SERVICE, BigDecimal.valueOf(30), 1)
        ));

        assertEquals(BigDecimal.valueOf(6.0), sale.commissionForBarber(0.2));
    }
}
