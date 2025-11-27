package com.empresa.barberia.repository;

import com.empresa.barberia.domain.Product;
import com.empresa.barberia.domain.StockMovement;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InventoryRepository {

    private final List<Product> products = new CopyOnWriteArrayList<>();
    private final List<StockMovement> movements = new CopyOnWriteArrayList<>();

    public InventoryRepository() {
        products.add(new Product(UUID.randomUUID().toString(), "Cera mate", "Retail", 12.0, 10, 3));
        products.add(new Product(UUID.randomUUID().toString(), "Shampoo menta", "Retail", 15.0, 8, 2));
    }

    public Mono<Product> create(String name, String category, double price, int stock) {
        var product = new Product(UUID.randomUUID().toString(), name, category, price, stock, Math.max(1, stock / 2));
        products.add(product);
        movements.add(new StockMovement(UUID.randomUUID().toString(), product.id(), stock, price, "PURCHASE", "Alta inicial", LocalDateTime.now()));
        return Mono.just(product);
    }

    public Flux<Product> findAll() {
        return Flux.fromIterable(products);
    }

    public Mono<Void> registerPurchase(String productId, int quantity, double unitPrice, String note) {
        return adjustStock(productId, quantity, unitPrice, note, "PURCHASE");
    }

    public Mono<Void> registerSale(String productId, int quantity, double unitPrice, String note) {
        return adjustStock(productId, -Math.abs(quantity), unitPrice, note, "SALE");
    }

    private Mono<Void> adjustStock(String productId, int quantity, double unitPrice, String note, String type) {
        var productOpt = findProduct(productId);
        if (productOpt.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Producto no encontrado"));
        }

        var product = productOpt.get();
        var newStock = product.stock() + quantity;
        if (newStock < 0) {
            return Mono.error(new IllegalStateException("No hay stock suficiente"));
        }

        products.remove(product);
        products.add(new Product(product.id(), product.name(), product.category(), unitPrice > 0 ? unitPrice : product.price(), newStock, product.minStock()));
        movements.add(new StockMovement(UUID.randomUUID().toString(), productId, quantity, unitPrice, type, note, LocalDateTime.now()));
        return Mono.empty();
    }

    public List<StockMovement> getMovements() {
        return new ArrayList<>(movements);
    }

    public Optional<Product> findProduct(String productId) {
        return products.stream().filter(p -> p.id().equals(productId)).findFirst();
    }
}
