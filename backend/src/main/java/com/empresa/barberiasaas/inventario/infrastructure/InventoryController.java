package com.empresa.barberiasaas.inventario.infrastructure;

import com.empresa.barberiasaas.inventario.application.InventoryService;
import com.empresa.barberiasaas.inventario.domain.Product;
import com.empresa.barberiasaas.inventario.domain.SalesProjection;
import com.empresa.barberiasaas.inventario.domain.StockMovement;
import com.empresa.barberiasaas.inventario.domain.StockSnapshot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/productos")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest request) {
        Product product = inventoryService.createProduct(request.name(), request.category(), request.price(), request.stock());
        return ResponseEntity.ok(product);
    }

    @PostMapping("/movimientos/compra")
    public ResponseEntity<StockMovement> registerPurchase(@RequestBody MovementRequest request) {
        StockMovement movement = inventoryService.registerPurchase(request.productId(), request.quantity(), request.unitPrice(), request.note());
        return ResponseEntity.ok(movement);
    }

    @PostMapping("/movimientos/venta")
    public ResponseEntity<StockMovement> registerSale(@RequestBody MovementRequest request) {
        StockMovement movement = inventoryService.registerSale(request.productId(), request.quantity(), request.unitPrice(), request.note());
        return ResponseEntity.ok(movement);
    }

    @GetMapping("/productos")
    public ResponseEntity<List<Product>> listProducts() {
        return ResponseEntity.ok(inventoryService.listProducts());
    }

    @GetMapping("/reportes/stock")
    public ResponseEntity<List<StockSnapshot>> stockReport() {
        return ResponseEntity.ok(inventoryService.stockReport());
    }

    @GetMapping("/reportes/ventas")
    public ResponseEntity<List<SalesProjection>> salesReport() {
        return ResponseEntity.ok(inventoryService.salesReport());
    }
}
