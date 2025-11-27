package com.empresa.barberia.api.v1;

import com.empresa.barberia.domain.Product;

public record ProductResponse(String id, String name, String category, double price, int stock) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.id(), product.name(), product.category(), product.price(), product.stock());
    }
}
