package com.empresa.barberiasaas.inventario.application;

import com.empresa.barberiasaas.inventario.domain.MovementType;
import com.empresa.barberiasaas.inventario.domain.Product;
import com.empresa.barberiasaas.inventario.domain.SalesProjection;
import com.empresa.barberiasaas.inventario.domain.StockMovement;
import com.empresa.barberiasaas.inventario.domain.StockSnapshot;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class InventoryService {

    private final Map<UUID, Product> products = new ConcurrentHashMap<>();
    private final List<StockMovement> movements = new CopyOnWriteArrayList<>();

    public Product createProduct(String name, String category, double price, int initialStock) {
        UUID id = UUID.randomUUID();
        Product product = new Product(id, name, category, price, initialStock);
        products.put(id, product);

        if (initialStock > 0) {
            movements.add(new StockMovement(UUID.randomUUID(), id, MovementType.PURCHASE, initialStock, price, LocalDateTime.now(), "Stock inicial"));
        }

        return product;
    }

    public StockMovement registerPurchase(UUID productId, int quantity, double unitPrice, String note) {
        Product product = getProductOrThrow(productId);
        Product updated = product.withStock(product.stock() + quantity);
        products.put(productId, updated);
        StockMovement movement = new StockMovement(UUID.randomUUID(), productId, MovementType.PURCHASE, quantity, unitPrice, LocalDateTime.now(), note);
        movements.add(movement);
        return movement;
    }

    public StockMovement registerSale(UUID productId, int quantity, double unitPrice, String note) {
        Product product = getProductOrThrow(productId);

        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad de venta debe ser mayor a cero");
        }

        if (product.stock() < quantity) {
            throw new IllegalArgumentException("Stock insuficiente para completar la venta");
        }

        Product updated = product.withStock(product.stock() - quantity);
        products.put(productId, updated);
        double priceToUse = unitPrice > 0 ? unitPrice : product.price();
        StockMovement movement = new StockMovement(UUID.randomUUID(), productId, MovementType.SALE, quantity, priceToUse, LocalDateTime.now(), note);
        movements.add(movement);
        return movement;
    }

    public List<Product> listProducts() {
        return new ArrayList<>(products.values());
    }

    public List<StockSnapshot> stockReport() {
        return products.values().stream()
                .sorted(Comparator.comparing(Product::name))
                .map(product -> new StockSnapshot(product.id(), product.name(), product.stock(), product.price(), product.category()))
                .toList();
    }

    public List<SalesProjection> salesReport() {
        Map<String, SalesProjection> aggregation = new ConcurrentHashMap<>();

        movements.stream()
                .filter(movement -> movement.type() == MovementType.SALE)
                .forEach(movement -> {
                    Product product = products.get(movement.productId());
                    String productName = product != null ? product.name() : "Producto";
                    SalesProjection current = aggregation.getOrDefault(productName, new SalesProjection(productName, 0, 0));
                    int quantity = current.quantitySold() + movement.quantity();
                    double total = current.totalAmount() + movement.quantity() * movement.unitPrice();
                    aggregation.put(productName, new SalesProjection(productName, quantity, total));
                });

        return aggregation.values().stream()
                .sorted(Comparator.comparing(SalesProjection::productName))
                .toList();
    }

    private Product getProductOrThrow(UUID id) {
        Product product = products.get(id);
        if (product == null) {
            throw new IllegalArgumentException("Producto no encontrado");
        }
        return product;
    }
}
