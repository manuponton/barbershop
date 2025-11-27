package com.empresa.barberia.repository;

import com.empresa.barberia.domain.Appointment;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class AppointmentRepository {

    private final List<Appointment> appointments = new CopyOnWriteArrayList<>();

    public Mono<Appointment> save(Appointment appointment) {
        appointments.add(appointment);
        return Mono.just(appointment);
    }

    public Flux<Appointment> findAll() {
        return Flux.fromIterable(appointments)
                .sort(Comparator.comparing(Appointment::startAt));
    }

    public boolean hasOverlap(Appointment candidate) {
        return appointments.stream()
                .filter(existing -> existing.barberId().equals(candidate.barberId()))
                .filter(existing -> !existing.status().equalsIgnoreCase("CANCELLED"))
                .anyMatch(existing -> existing.startAt().isBefore(candidate.endsAt()) && candidate.startAt().isBefore(existing.endsAt()));
    }

    public Flux<Appointment> findByFilters(String barberId, LocalDateTime start, LocalDateTime end) {
        return Flux.fromIterable(appointments)
                .filter(appointment -> barberId == null || appointment.barberId().equals(barberId))
                .filter(appointment -> (start == null || !appointment.startAt().isBefore(start))
                        && (end == null || !appointment.startAt().isAfter(end)))
                .sort(Comparator.comparing(Appointment::startAt));
    }
}
