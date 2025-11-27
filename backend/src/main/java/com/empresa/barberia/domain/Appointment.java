package com.empresa.barberia.domain;

import java.time.LocalDateTime;

public record Appointment(
        String id,
        String clientId,
        String barberId,
        String clientName,
        String barberName,
        String serviceId,
        String service,
        LocalDateTime startAt,
        int durationMinutes,
        String status,
        String sucursalId
) {
    public LocalDateTime endsAt() {
        return startAt.plusMinutes(durationMinutes);
    }
}
