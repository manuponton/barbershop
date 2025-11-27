package com.empresa.barberia.domain;

public record Product(String id, String name, String category, double price, int stock, int minStock) {
}
