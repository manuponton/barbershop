package com.empresa.barberia.service;

import com.empresa.barberia.domain.Appointment;
import com.empresa.barberia.repository.ServiceCatalogRepository;
import com.empresa.barberia.repository.AppointmentRepository;
import com.empresa.barberia.repository.BarberRepository;
import com.empresa.barberia.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BarberRepository barberRepository;
    private final ClientRepository clientRepository;
    private final ServiceCatalogRepository serviceCatalogRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, BarberRepository barberRepository, ClientRepository clientRepository, ServiceCatalogRepository serviceCatalogRepository) {
        this.appointmentRepository = appointmentRepository;
        this.barberRepository = barberRepository;
        this.clientRepository = clientRepository;
        this.serviceCatalogRepository = serviceCatalogRepository;
    }

    public Mono<Appointment> createAppointment(String clientName, String barberName, String service, LocalDateTime startAt, int durationMinutes, String sucursalId) {
        return Mono.defer(() -> {
            var client = clientRepository.findByName(clientName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente no encontrado"));
            var barber = barberRepository.findByName(barberName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Barbero no encontrado"));

            var serviceMatch = serviceCatalogRepository.findAll()
                    .filter(s -> s.name().equalsIgnoreCase(service))
                    .next()
                    .blockOptional()
                    .orElse(null);

            if (startAt.isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de inicio debe ser futura");
            }

            var appointment = new Appointment(
                    UUID.randomUUID().toString(),
                    client.id(),
                    barber.id(),
                    client.name(),
                    barber.name(),
                    serviceMatch != null ? serviceMatch.id() : null,
                    service,
                    startAt,
                    durationMinutes,
                    "BOOKED",
                    sucursalId != null ? sucursalId : client.sucursalId()
            );

            if (appointmentRepository.hasOverlap(appointment)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El barbero ya tiene una cita en ese horario");
            }

            return appointmentRepository.save(appointment);
        });
    }

    public Flux<Appointment> listAppointments() {
        return appointmentRepository.findAll();
    }

    public Flux<Appointment> listByFilters(String barberId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByFilters(barberId, start, end);
    }
}
