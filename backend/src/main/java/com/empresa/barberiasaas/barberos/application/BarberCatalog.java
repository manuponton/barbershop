package com.empresa.barberiasaas.barberos.application;

import com.empresa.barberiasaas.barberos.domain.Barber;
import com.empresa.barberiasaas.barberos.infrastructure.persistence.BarberEntity;
import com.empresa.barberiasaas.barberos.infrastructure.persistence.BarberRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BarberCatalog {

    private final BarberRepository barberRepository;

    public BarberCatalog(BarberRepository barberRepository) {
        this.barberRepository = barberRepository;
    }

    public List<Barber> all() {
        return barberRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private Barber toDomain(BarberEntity entity) {
        var services = entity.getServicesCsv() == null || entity.getServicesCsv().isBlank()
                ? List.<String>of()
                : Arrays.stream(entity.getServicesCsv().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        return new Barber(entity.getId(), entity.getSucursal().getId(), entity.getName(), services);
    }
}
