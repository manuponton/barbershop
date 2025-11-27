package com.empresa.barberiasaas.clientes.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientSegment(
        UUID id,
        UUID clientId,
        String name,
        String criteria,
        LocalDateTime segmentedAt,
        LocalDateTime nextReminderAt,
        String reminderNote
) {
}
