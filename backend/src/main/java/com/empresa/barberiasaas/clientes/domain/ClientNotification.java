package com.empresa.barberiasaas.clientes.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientNotification(
        UUID id,
        UUID clientId,
        String category,
        String message,
        LocalDateTime scheduledAt,
        boolean delivered
) {
}
