package com.empresa.barberiasaas.caja.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public record SaleItem(UUID referenceId, String description, ItemType type, BigDecimal unitPrice, int quantity) {

    public enum ItemType {SERVICE, PRODUCT}

    public SaleItem {
        Objects.requireNonNull(referenceId, "referenceId");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(unitPrice, "unitPrice");
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
    }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
