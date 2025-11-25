package com.empresa.barberiasaas.barberos.application;

import com.empresa.barberiasaas.barberos.domain.Barber;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BarberCatalog {

    private final List<Barber> barbers = new ArrayList<>();

    @PostConstruct
    void seed() {
        if (!barbers.isEmpty()) {
            return;
        }
        barbers.add(new Barber(UUID.randomUUID(), "Carlos", List.of("Corte clásico", "Arreglo de barba")));
        barbers.add(new Barber(UUID.randomUUID(), "María", List.of("Fade", "Color")));
    }

    public List<Barber> all() {
        return List.copyOf(barbers);
    }
}
