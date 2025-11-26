package com.empresa.barberia.domain;

import java.time.LocalDateTime;

public record Appointment(
        String id,
        String clientId,
        String barberId,
        String clientName,
        String barberName,
        String service,
        LocalDateTime startAt,
        int durationMinutes
) {
}
