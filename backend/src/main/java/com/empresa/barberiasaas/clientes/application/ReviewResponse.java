package com.empresa.barberiasaas.clientes.application;

import com.empresa.barberiasaas.clientes.domain.ClientReview;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        UUID clientId,
        int rating,
        String comment,
        String channel,
        LocalDateTime createdAt,
        LocalDateTime followUpAt
) {
    public static ReviewResponse from(ClientReview review) {
        return new ReviewResponse(
                review.id(),
                review.clientId(),
                review.rating(),
                review.comment(),
                review.channel().name(),
                review.createdAt(),
                review.followUpAt()
        );
    }
}
