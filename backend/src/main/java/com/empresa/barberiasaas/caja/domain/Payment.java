package com.empresa.barberiasaas.caja.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Payment(UUID id, String description, double amount, PaymentMethod method, PaymentStatus status, String authorization, LocalDateTime processedAt) {
}
