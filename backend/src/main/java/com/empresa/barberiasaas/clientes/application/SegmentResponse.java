package com.empresa.barberiasaas.clientes.application;

import com.empresa.barberiasaas.clientes.domain.ClientSegment;

import java.time.LocalDateTime;
import java.util.UUID;

public record SegmentResponse(
        UUID id,
        UUID clientId,
        String segment,
        String criteria,
        LocalDateTime segmentedAt,
        LocalDateTime nextReminderAt,
        String reminderNote
) {
    public static SegmentResponse from(ClientSegment segment) {
        return new SegmentResponse(
                segment.id(),
                segment.clientId(),
                segment.name(),
                segment.criteria(),
                segment.segmentedAt(),
                segment.nextReminderAt(),
                segment.reminderNote()
        );
    }
}
