package com.empresa.barberia.api.v1;

public record CatalogItemResponse(String id, String nombre, String descripcion, String categoria, double precio, Integer duracionMinutos) {
}
