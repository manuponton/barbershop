package com.empresa.barberia.repository;

import com.empresa.barberia.domain.Barber;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BarberRepository {

    private final List<Barber> barbers = List.of(
            new Barber(UUID.randomUUID().toString(), "Diego Cruz", List.of("Corte fade", "Barba y bigote", "Color")),
            new Barber(UUID.randomUUID().toString(), "Mariana Torres", List.of("Corte clásico", "Cejas", "Tratamientos")),
            new Barber(UUID.randomUUID().toString(), "Luis Herrera", List.of("Barbería premium", "Diseños", "Perfilado"))
    );

    public Flux<Barber> findAll() {
        return Flux.fromIterable(barbers);
    }

    public Optional<Barber> findByName(String name) {
        return barbers.stream().filter(barber -> barber.name().equalsIgnoreCase(name)).findFirst();
    }
}
