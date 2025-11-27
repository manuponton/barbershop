package com.empresa.barberiasaas.barberos.application;

import com.empresa.barberiasaas.barberos.domain.Barber;
import com.empresa.barberiasaas.sucursales.application.SucursalDirectory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BarberCatalog {

    private final List<Barber> barbers = new ArrayList<>();
    private final SucursalDirectory sucursalDirectory;

    public BarberCatalog(SucursalDirectory sucursalDirectory) {
        this.sucursalDirectory = sucursalDirectory;
    }

    @PostConstruct
    void seed() {
        if (!barbers.isEmpty()) {
            return;
        }
        var sucursales = sucursalDirectory.all();
        if (sucursales.isEmpty()) {
            return;
        }
        var centro = sucursales.get(0);
        var norte = sucursales.size() > 1 ? sucursales.get(1) : centro;

        barbers.add(new Barber(UUID.randomUUID(), centro.id(), "Carlos", List.of("Corte clásico", "Arreglo de barba")));
        barbers.add(new Barber(UUID.randomUUID(), centro.id(), "María", List.of("Fade", "Color")));
        barbers.add(new Barber(UUID.randomUUID(), norte.id(), "Lucía", List.of("Color fantasía", "Keratina")));
    }

    public List<Barber> all() {
        return List.copyOf(barbers);
    }
}
