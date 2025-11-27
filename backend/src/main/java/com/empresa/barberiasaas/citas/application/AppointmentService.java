package com.empresa.barberiasaas.citas.application;

import com.empresa.barberiasaas.citas.domain.Appointment;
import com.empresa.barberiasaas.citas.infrastructure.persistence.AppointmentEntity;
import com.empresa.barberiasaas.citas.infrastructure.persistence.AppointmentRepository;
import com.empresa.barberiasaas.sucursales.application.SucursalDirectory;
import com.empresa.barberiasaas.sucursales.infrastructure.persistence.SucursalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    private final SucursalDirectory sucursalDirectory;
    private final AppointmentRepository appointmentRepository;
    private final SucursalRepository sucursalRepository;

    public AppointmentService(SucursalDirectory sucursalDirectory, AppointmentRepository appointmentRepository, SucursalRepository sucursalRepository) {
        this.sucursalDirectory = sucursalDirectory;
        this.appointmentRepository = appointmentRepository;
        this.sucursalRepository = sucursalRepository;
    }

    public Appointment create(String clientName, String barberName, String service, LocalDateTime startAt, Duration duration, UUID sucursalId) {
        var sucursal = sucursalDirectory.byId(sucursalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sucursal no encontrada"));
        var allows = sucursal.availability().stream().anyMatch(rule -> rule.allows(startAt));
        if (!allows) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sucursal no permite reservas en ese horario");
        }

        var sucursalEntity = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sucursal no encontrada"));

        var entity = new AppointmentEntity(
                UUID.randomUUID(),
                startAt,
                Math.toIntExact(duration.toMinutes()),
                clientName,
                barberName,
                service,
                sucursalEntity
        );

        appointmentRepository.save(entity);

        return toDomain(entity, sucursalId);
    }

    public List<Appointment> upcoming(LocalDateTime from) {
        return appointmentRepository.findByStartAtAfterOrderByStartAtAsc(from).stream()
                .map(this::toDomain)
                .sorted(Comparator.comparing(Appointment::startAt))
                .toList();
    }

    public List<Appointment> all() {
        return appointmentRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private Appointment toDomain(AppointmentEntity entity) {
        return toDomain(entity, entity.getSucursal().getId());
    }

    private Appointment toDomain(AppointmentEntity entity, UUID sucursalId) {
        return new Appointment(
                entity.getId(),
                entity.getStartAt(),
                Duration.ofMinutes(entity.getDurationMinutes()),
                entity.getClientName(),
                entity.getBarberName(),
                entity.getServiceName(),
                sucursalId
        );
    }
}
