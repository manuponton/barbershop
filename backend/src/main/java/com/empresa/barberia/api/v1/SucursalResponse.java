package com.empresa.barberia.api.v1;

import com.empresa.barberia.domain.AvailabilityRule;
import com.empresa.barberia.domain.Branch;
import com.empresa.barberia.domain.Branding;

import java.util.List;

public record SucursalResponse(
        String id,
        String nombre,
        String slug,
        String direccion,
        String ciudad,
        Branding branding,
        List<AvailabilityRule> disponibilidad
) {
    public static SucursalResponse from(Branch branch) {
        return new SucursalResponse(branch.id(), branch.nombre(), branch.slug(), branch.direccion(), branch.ciudad(), branch.branding(), branch.disponibilidad());
    }
}
