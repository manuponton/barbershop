package com.empresa.barberiasaas.caja.application;

import com.empresa.barberiasaas.caja.domain.CashRegisterSession;
import com.empresa.barberiasaas.caja.domain.Payment;
import com.empresa.barberiasaas.caja.domain.PaymentMethod;
import com.empresa.barberiasaas.caja.domain.PaymentStatus;
import com.empresa.barberiasaas.caja.domain.SalesReport;
import com.empresa.barberiasaas.caja.infrastructure.PaymentGatewayClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CashRegisterService {

    private final PaymentGatewayClient paymentGatewayClient;
    private CashRegisterSession currentSession;
    private final List<CashRegisterSession> sessions = new CopyOnWriteArrayList<>();
    private final List<Payment> payments = new CopyOnWriteArrayList<>();

    public CashRegisterService(PaymentGatewayClient paymentGatewayClient) {
        this.paymentGatewayClient = paymentGatewayClient;
    }

    public CashRegisterSession openSession(double openingAmount) {
        if (currentSession != null && currentSession.isOpen()) {
            throw new IllegalStateException("Ya existe una caja abierta");
        }

        currentSession = new CashRegisterSession(UUID.randomUUID(), LocalDateTime.now(), null, openingAmount, null);
        sessions.add(currentSession);
        return currentSession;
    }

    public CashRegisterSession closeSession(double closingAmount) {
        if (currentSession == null || !currentSession.isOpen()) {
            throw new IllegalStateException("No hay una caja abierta para cerrar");
        }

        currentSession = new CashRegisterSession(currentSession.id(), currentSession.openedAt(), LocalDateTime.now(), currentSession.openingAmount(), closingAmount);
        sessions.add(currentSession);
        return currentSession;
    }

    public Payment registerPayment(String description, double amount, PaymentMethod method) {
        if (currentSession == null || !currentSession.isOpen()) {
            throw new IllegalStateException("Debe abrir caja antes de registrar pagos");
        }

        PaymentStatus status = paymentGatewayClient.process(description, amount, method);
        Payment payment = new Payment(UUID.randomUUID(), description, amount, method, status, generateAuthorization(status), LocalDateTime.now());
        payments.add(payment);
        return payment;
    }

    public SalesReport salesReport() {
        double total = payments.stream()
                .filter(payment -> payment.status() == PaymentStatus.APPROVED)
                .mapToDouble(Payment::amount)
                .sum();

        long approved = payments.stream().filter(payment -> payment.status() == PaymentStatus.APPROVED).count();
        long declined = payments.stream().filter(payment -> payment.status() == PaymentStatus.DECLINED).count();

        return new SalesReport(total, approved, declined, new ArrayList<>(payments));
    }

    public CashRegisterSession currentSession() {
        return currentSession;
    }

    private String generateAuthorization(PaymentStatus status) {
        return status == PaymentStatus.APPROVED ? "AUTH-" + UUID.randomUUID().toString().substring(0, 8) : "DECLINED";
    }
}
