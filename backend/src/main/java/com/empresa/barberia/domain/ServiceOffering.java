package com.empresa.barberia.domain;

public record ServiceOffering(String id, String name, int durationMinutes, double price, String sucursalId) {
}
