package com.empresa.barberiasaas.clientes.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientReview(
        UUID id,
        UUID clientId,
        int rating,
        String comment,
        ReviewChannel channel,
        LocalDateTime createdAt,
        LocalDateTime followUpAt
) {
}
