package com.empresa.barberia.api.v1;

public record ProductPayload(String name, String category, double price, int stock) {
}
