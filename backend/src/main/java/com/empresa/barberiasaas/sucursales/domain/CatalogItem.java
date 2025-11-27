package com.empresa.barberiasaas.sucursales.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record CatalogItem(UUID id, UUID branchId, String name, String description, String category, BigDecimal price, Integer durationMinutes) {
}
