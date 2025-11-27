package com.empresa.barberiasaas.citas.application;

import com.empresa.barberiasaas.citas.domain.Appointment;
import com.empresa.barberiasaas.sucursales.application.SucursalDirectory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private final SucursalDirectory sucursalDirectory;

    public AppointmentService(SucursalDirectory sucursalDirectory) {
        this.sucursalDirectory = sucursalDirectory;
    }

    public Appointment create(String clientName, String barberName, String service, LocalDateTime startAt, Duration duration, UUID sucursalId) {
        var sucursal = sucursalDirectory.byId(sucursalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sucursal no encontrada"));
        var allows = sucursal.availability().stream().anyMatch(rule -> rule.allows(startAt));
        if (!allows) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sucursal no permite reservas en ese horario");
        }

        Appointment appointment = new Appointment(UUID.randomUUID(), startAt, duration, clientName, barberName, service, sucursalId);
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
