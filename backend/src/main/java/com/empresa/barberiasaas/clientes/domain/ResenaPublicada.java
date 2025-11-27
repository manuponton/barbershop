package com.empresa.barberiasaas.clientes.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResenaPublicada(UUID clientId, int rating, String canal, LocalDateTime occurredOn) implements DomainEvent {
    @Override
    public String type() {
        return "ReseñaPublicada";
    }
}
