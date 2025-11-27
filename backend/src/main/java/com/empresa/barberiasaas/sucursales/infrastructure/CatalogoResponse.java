package com.empresa.barberiasaas.sucursales.infrastructure;

import com.empresa.barberiasaas.sucursales.domain.CatalogItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CatalogoResponse(UUID sucursalId, List<ItemResponse> items) {

    public static CatalogoResponse from(UUID sucursalId, List<CatalogItem> catalogItems) {
        return new CatalogoResponse(sucursalId, catalogItems.stream().map(ItemResponse::from).toList());
    }

    public record ItemResponse(UUID id, String nombre, String descripcion, String categoria, BigDecimal precio, Integer duracionMinutos) {
        public static ItemResponse from(CatalogItem item) {
            return new ItemResponse(item.id(), item.name(), item.description(), item.category(), item.price(), item.durationMinutes());
        }
    }
}
