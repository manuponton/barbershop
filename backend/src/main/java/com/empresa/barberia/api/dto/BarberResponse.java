package com.empresa.barberia.api.dto;

import com.empresa.barberia.domain.Barber;

import java.util.List;

public record BarberResponse(String id, String name, String sucursalId, List<String> services) {
    public static BarberResponse from(Barber barber) {
        return new BarberResponse(barber.id(), barber.name(), barber.sucursalId(), barber.services());
    }
}
