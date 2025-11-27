package com.empresa.barberiasaas.sucursales.infrastructure;

import com.empresa.barberiasaas.sucursales.domain.AvailabilityRule;
import com.empresa.barberiasaas.sucursales.domain.BrandingConfig;
import com.empresa.barberiasaas.sucursales.domain.Sucursal;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record SucursalResponse(UUID id, String nombre, String slug, String direccion, String ciudad,
                               BrandingConfig branding, List<AvailabilityResponse> disponibilidad) {

    public static SucursalResponse from(Sucursal sucursal) {
        return new SucursalResponse(
                sucursal.id(),
                sucursal.nombre(),
                sucursal.slug(),
                sucursal.direccion(),
                sucursal.ciudad(),
                sucursal.branding(),
                sucursal.availability().stream().map(AvailabilityResponse::from).toList()
        );
    }

    public record AvailabilityResponse(DayOfWeek dia, LocalTime abre, LocalTime cierra, boolean online) {
        public static AvailabilityResponse from(AvailabilityRule rule) {
            return new AvailabilityResponse(rule.dayOfWeek(), rule.opensAt(), rule.closesAt(), rule.onlineBookingEnabled());
        }
    }
}
