package com.empresa.barberia.api.v1;

import com.empresa.barberia.domain.StockMovement;
import com.empresa.barberia.repository.InventoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping("/productos")
    public Flux<ProductResponse> listProducts() {
        return inventoryRepository.findAll().map(ProductResponse::from);
    }

    @PostMapping("/productos")
    public Mono<ProductResponse> createProduct(@RequestBody ProductPayload payload) {
        return inventoryRepository.create(payload.name(), payload.category(), payload.price(), payload.stock())
                .map(ProductResponse::from);
    }

    @PostMapping("/movimientos/compra")
    public Mono<Void> registerPurchase(@RequestBody MovementPayload payload) {
        return inventoryRepository.registerPurchase(payload.productId(), payload.quantity(), payload.unitPrice(), payload.note());
    }

    @PostMapping("/movimientos/venta")
    public Mono<Void> registerSale(@RequestBody MovementPayload payload) {
        return inventoryRepository.registerSale(payload.productId(), payload.quantity(), payload.unitPrice(), payload.note());
    }

    @GetMapping("/reportes/stock")
    public Flux<StockSnapshotResponse> stockReport() {
        return inventoryRepository.findAll()
                .map(product -> new StockSnapshotResponse(product.id(), product.name(), product.stock(), product.price(), product.category(), product.stock() <= product.minStock()));
    }

    @GetMapping("/reportes/ventas")
    public Flux<SalesProjectionResponse> salesReport() {
        var totals = inventoryRepository.getMovements().stream()
                .filter(movement -> movement.type().equalsIgnoreCase("SALE"))
                .collect(Collectors.groupingBy(StockMovement::productId, Collectors.summingInt(StockMovement::quantity)));

        return Flux.fromIterable(inventoryRepository.getMovements())
                .filter(movement -> movement.type().equalsIgnoreCase("SALE"))
                .collect(Collectors.groupingBy(StockMovement::productId, Collectors.summarizingDouble(m -> m.unitPrice() * Math.abs(m.quantity()))))
                .flatMapMany(summary -> inventoryRepository.findAll()
                        .collect(Collectors.toMap(product -> product.id(), product -> product))
                        .flatMapMany(productMap -> Flux.fromIterable(summary.entrySet())
                                .map(entry -> {
                                    var product = productMap.get(entry.getKey());
                                    var quantitySold = Math.abs(totals.getOrDefault(entry.getKey(), 0));
                                    return new SalesProjectionResponse(product != null ? product.name() : "Producto", quantitySold, entry.getValue().getSum());
                                })));
    }
}
