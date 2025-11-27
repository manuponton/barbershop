package com.empresa.barberiasaas.clientes.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccionDeFidelizacionAplicada(UUID clientId, String programa, int puntos, LocalDateTime occurredOn) implements DomainEvent {
    @Override
    public String type() {
        return "AccionDeFidelizacionAplicada";
    }
}
