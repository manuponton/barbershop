package com.empresa.barberiasaas.caja.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Sale {

    private final UUID id;
    private final UUID barberId;
    private final UUID customerId;
    private final List<SaleItem> items = new ArrayList<>();

    public Sale(UUID id, UUID barberId, UUID customerId, List<SaleItem> initialItems) {
        this.id = id;
        this.barberId = barberId;
        this.customerId = customerId;
        if (initialItems != null) {
            this.items.addAll(initialItems);
        }
    }

    public UUID id() {
        return id;
    }

    public UUID barberId() {
        return barberId;
    }

    public UUID customerId() {
        return customerId;
    }

    public List<SaleItem> items() {
        return Collections.unmodifiableList(items);
    }

    public Sale addItem(SaleItem item) {
        items.add(item);
        return this;
    }

    public BigDecimal total() {
        return items.stream()
                .map(SaleItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal commissionForBarber(double percentage) {
        return total().multiply(BigDecimal.valueOf(percentage));
    }
}
