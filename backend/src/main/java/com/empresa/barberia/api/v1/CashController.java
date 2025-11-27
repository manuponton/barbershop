package com.empresa.barberia.api.v1;

import com.empresa.barberia.repository.CashRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/caja")
public class CashController {

    private final CashRepository cashRepository;

    public CashController(CashRepository cashRepository) {
        this.cashRepository = cashRepository;
    }

    @GetMapping("/estado")
    public Mono<CashSessionResponse> status() {
        return cashRepository.getCurrentSession().map(CashSessionResponse::from).map(Mono::just).orElse(Mono.empty());
    }

    @PostMapping("/aperturas")
    public Mono<CashSessionResponse> open(@RequestBody CashOpeningRequest request) {
        return cashRepository.open(request.openingAmount()).map(CashSessionResponse::from);
    }

    @PostMapping("/cierres")
    public Mono<CashSessionResponse> close(@RequestBody CashClosingRequest request) {
        return cashRepository.close(request.closingAmount()).map(CashSessionResponse::from);
    }

    @PostMapping("/pagos")
    public Mono<PaymentResponse> registerPayment(@RequestBody PaymentPayload payload) {
        return cashRepository.registerPayment(payload.description(), payload.amount(), payload.method())
                .map(PaymentResponse::from);
    }

    @GetMapping("/reportes/ventas")
    public Mono<SalesReportResponse> salesReport() {
        return cashRepository.listPayments()
                .collectList()
                .map(payments -> new SalesReportResponse(
                        payments.stream().mapToDouble(payment -> payment.amount()).sum(),
                        payments.stream().filter(payment -> payment.status().equalsIgnoreCase("APPROVED")).count(),
                        payments.stream().filter(payment -> payment.status().equalsIgnoreCase("DECLINED")).count(),
                        payments.stream().map(PaymentResponse::from).toList()
                ));
    }
}
