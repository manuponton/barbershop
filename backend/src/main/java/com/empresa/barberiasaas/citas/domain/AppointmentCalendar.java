package com.empresa.barberiasaas.citas.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AppointmentCalendar {

    private final List<AppointmentDetails> appointments = new ArrayList<>();

    public AppointmentDetails schedule(AppointmentDetails appointment) {
        ensureNoOverlap(appointment.barberId(), appointment.startAt(), appointment.endAt());
        appointments.add(appointment);
        return appointment;
    }

    public List<AppointmentDetails> appointments() {
        return Collections.unmodifiableList(appointments);
    }

    private void ensureNoOverlap(UUID barberId, LocalDateTime start, LocalDateTime end) {
        boolean overlaps = appointments.stream()
                .filter(app -> app.barberId().equals(barberId))
                .filter(app -> app.status() != AppointmentStatus.CANCELLED && app.status() != AppointmentStatus.NO_SHOW)
                .anyMatch(app -> start.isBefore(app.endAt()) && end.isAfter(app.startAt()));
        if (overlaps) {
            throw new IllegalArgumentException("El barbero ya tiene una cita en esa franja");
        }
    }
}
