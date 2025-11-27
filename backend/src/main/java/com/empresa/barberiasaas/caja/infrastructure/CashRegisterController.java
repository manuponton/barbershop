package com.empresa.barberiasaas.caja.infrastructure;

import com.empresa.barberiasaas.caja.application.CashRegisterService;
import com.empresa.barberiasaas.caja.domain.CashRegisterSession;
import com.empresa.barberiasaas.caja.domain.Payment;
import com.empresa.barberiasaas.caja.domain.SalesReport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/caja")
public class CashRegisterController {

    private final CashRegisterService cashRegisterService;

    public CashRegisterController(CashRegisterService cashRegisterService) {
        this.cashRegisterService = cashRegisterService;
    }

    @PostMapping("/aperturas")
    public ResponseEntity<CashRegisterSession> open(@RequestBody OpenSessionRequest request) {
        return ResponseEntity.ok(cashRegisterService.openSession(request.openingAmount()));
    }

    @PostMapping("/cierres")
    public ResponseEntity<CashRegisterSession> close(@RequestBody CloseSessionRequest request) {
        return ResponseEntity.ok(cashRegisterService.closeSession(request.closingAmount()));
    }

    @PostMapping("/pagos")
    public ResponseEntity<Payment> registerPayment(@RequestBody PaymentRequest request) {
        Payment payment = cashRegisterService.registerPayment(request.description(), request.amount(), request.method());
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/estado")
    public ResponseEntity<CashRegisterSession> status() {
        return ResponseEntity.ok(cashRegisterService.currentSession());
    }

    @GetMapping("/reportes/ventas")
    public ResponseEntity<SalesReport> salesReport() {
        return ResponseEntity.ok(cashRegisterService.salesReport());
    }
}
