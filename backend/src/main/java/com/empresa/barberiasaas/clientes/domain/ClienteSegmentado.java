package com.empresa.barberiasaas.clientes.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteSegmentado(UUID clientId, String segmento, String criterio, LocalDateTime occurredOn) implements DomainEvent {
    @Override
    public String type() {
        return "ClienteSegmentado";
    }
}
