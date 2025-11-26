package com.empresa.barberia.domain;

import java.util.List;

public record Barber(String id, String name, List<String> services) {
}
