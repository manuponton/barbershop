package com.empresa.barberia.repository;

import com.empresa.barberia.domain.CashSession;
import com.empresa.barberia.domain.Payment;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CashRepository {

    private CashSession currentSession;
    private final List<Payment> payments = new ArrayList<>();

    public Mono<CashSession> open(double openingAmount) {
        if (currentSession != null && currentSession.isOpen()) {
            return Mono.error(new IllegalStateException("Ya existe una caja abierta"));
        }
        currentSession = new CashSession(UUID.randomUUID().toString(), LocalDateTime.now(), null, openingAmount, null);
        return Mono.just(currentSession);
    }

    public Mono<CashSession> close(double closingAmount) {
        if (currentSession == null || !currentSession.isOpen()) {
            return Mono.error(new IllegalStateException("No hay caja abierta"));
        }
        currentSession = new CashSession(currentSession.id(), currentSession.openedAt(), LocalDateTime.now(), currentSession.openingAmount(), closingAmount);
        return Mono.just(currentSession);
    }

    public Optional<CashSession> getCurrentSession() {
        return Optional.ofNullable(currentSession);
    }

    public Mono<Payment> registerPayment(String description, double amount, String method) {
        if (currentSession == null || !currentSession.isOpen()) {
            return Mono.error(new IllegalStateException("Debe abrir caja antes de cobrar"));
        }

        var status = amount > 0 ? "APPROVED" : "DECLINED";
        var payment = new Payment(UUID.randomUUID().toString(), description, amount, method, status, UUID.randomUUID().toString(), LocalDateTime.now());
        payments.add(payment);
        return Mono.just(payment);
    }

    public Flux<Payment> listPayments() {
        return Flux.fromIterable(payments);
    }
}
