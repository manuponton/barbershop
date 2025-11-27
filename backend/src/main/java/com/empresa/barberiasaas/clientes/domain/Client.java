package com.empresa.barberiasaas.clientes.domain;

import java.time.LocalDate;
import java.util.UUID;

public record Client(UUID id, UUID sucursalId, String name, String email, LocalDate birthday) {
}
