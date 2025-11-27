package com.empresa.barberiasaas.clientes.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoyaltyAction(
        UUID id,
        UUID clientId,
        String program,
        String reason,
        int points,
        String tier,
        LocalDateTime appliedAt,
        LocalDateTime reminderAt,
        String reminderMessage
) {
}
