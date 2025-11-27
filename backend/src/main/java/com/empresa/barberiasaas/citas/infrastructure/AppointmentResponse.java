package com.empresa.barberiasaas.citas.infrastructure;

import com.empresa.barberiasaas.citas.domain.Appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        String clientName,
        String barberName,
        String service,
        LocalDateTime startAt,
        long durationMinutes,
        UUID sucursalId
) {
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.id(),
                appointment.clientName(),
                appointment.barberName(),
                appointment.service(),
                appointment.startAt(),
                appointment.duration().toMinutes(),
                appointment.sucursalId()
        );
    }
}
