package com.empresa.barberiasaas.sucursales.domain;

import java.util.List;
import java.util.UUID;

public record Sucursal(UUID id, String nombre, String slug, String direccion, String ciudad,
                       BrandingConfig branding, List<AvailabilityRule> availability, List<CatalogItem> catalog) {
}
