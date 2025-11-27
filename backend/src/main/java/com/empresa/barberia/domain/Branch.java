package com.empresa.barberia.domain;

import java.util.List;

public record Branch(
        String id,
        String nombre,
        String slug,
        String direccion,
        String ciudad,
        Branding branding,
        List<AvailabilityRule> disponibilidad
) {
}
