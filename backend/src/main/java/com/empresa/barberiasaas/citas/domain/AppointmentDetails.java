package com.empresa.barberiasaas.citas.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record AppointmentDetails(
        UUID id,
        UUID barberId,
        UUID customerId,
        UUID serviceId,
        LocalDateTime startAt,
        Duration duration,
        AppointmentStatus status
) {
    public AppointmentDetails {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(barberId, "barberId");
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(serviceId, "serviceId");
        Objects.requireNonNull(startAt, "startAt");
        Objects.requireNonNull(duration, "duration");
        Objects.requireNonNull(status, "status");
    }

    public LocalDateTime endAt() {
        return startAt.plus(duration);
    }

    public AppointmentDetails reschedule(LocalDateTime newStart, Duration newDuration) {
        return new AppointmentDetails(id, barberId, customerId, serviceId, newStart, newDuration, status);
    }

    public AppointmentDetails updateStatus(AppointmentStatus newStatus) {
        return new AppointmentDetails(id, barberId, customerId, serviceId, startAt, duration, newStatus);
    }
}
