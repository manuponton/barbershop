package com.empresa.barberia.repository;

import com.empresa.barberia.domain.Appointment;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
}
