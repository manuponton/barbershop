package com.empresa.barberia.domain;

import java.time.LocalDateTime;

public record Payment(String id, String description, double amount, String method, String status, String authorization, LocalDateTime processedAt) {
}
