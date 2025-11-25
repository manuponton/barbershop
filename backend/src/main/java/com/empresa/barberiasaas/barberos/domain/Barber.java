package com.empresa.barberiasaas.barberos.domain;

import java.util.List;
import java.util.UUID;

public record Barber(UUID id, String name, List<String> services) {
}
