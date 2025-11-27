package com.empresa.barberia.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotBlank(message = "El cliente es requerido") String clientName,
        @NotBlank(message = "El barbero es requerido") String barberName,
        @NotBlank(message = "El servicio es requerido") String service,
        @NotNull(message = "La fecha de inicio es requerida")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startAt,
        @Min(value = 10, message = "La duración mínima es de 10 minutos") int durationMinutes,
        String sucursalId
) {
}
