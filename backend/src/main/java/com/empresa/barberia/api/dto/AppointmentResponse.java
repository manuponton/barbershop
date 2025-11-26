package com.empresa.barberia.api.dto;

import com.empresa.barberia.domain.Appointment;

import java.time.LocalDateTime;

public record AppointmentResponse(
        String id,
        String clientName,
        String barberName,
        String service,
        LocalDateTime startAt,
        int durationMinutes
) {
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.id(),
                appointment.clientName(),
                appointment.barberName(),
                appointment.service(),
                appointment.startAt(),
                appointment.durationMinutes()
        );
    }
}
