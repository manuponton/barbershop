package com.empresa.barberiasaas.barberos.application;

import com.empresa.barberiasaas.barberos.domain.Barber;

import java.util.UUID;

public record BarberResponse(UUID id, UUID sucursalId, String name, java.util.List<String> services) {
    public static BarberResponse from(Barber barber) {
        return new BarberResponse(barber.id(), barber.sucursalId(), barber.name(), barber.services());
    }
}
