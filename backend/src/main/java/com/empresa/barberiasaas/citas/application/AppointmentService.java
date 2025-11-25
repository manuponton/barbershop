package com.empresa.barberiasaas.citas.application;

import com.empresa.barberiasaas.citas.domain.Appointment;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AppointmentService {

    private final Map<UUID, Appointment> appointments = new ConcurrentHashMap<>();

    public Appointment create(String clientName, String barberName, String service, LocalDateTime startAt, Duration duration) {
        Appointment appointment = new Appointment(UUID.randomUUID(), startAt, duration, clientName, barberName, service);
        appointments.put(appointment.id(), appointment);
        return appointment;
    }

    public List<Appointment> upcoming(LocalDateTime from) {
        return appointments.values().stream()
                .filter(appointment -> !appointment.startAt().isBefore(from))
                .sorted(Comparator.comparing(Appointment::startAt))
                .toList();
    }

    public List<Appointment> all() {
        return new ArrayList<>(appointments.values());
    }
}
