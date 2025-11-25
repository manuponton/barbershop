package com.empresa.barberiasaas.citas.infrastructure;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Duration;
import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotBlank String clientName,
        @NotBlank String barberName,
        @NotBlank String service,
        @NotNull @FutureOrPresent LocalDateTime startAt,
        @NotNull @Positive long durationMinutes
) {
    public Duration duration() {
        return Duration.ofMinutes(durationMinutes);
    }
}
