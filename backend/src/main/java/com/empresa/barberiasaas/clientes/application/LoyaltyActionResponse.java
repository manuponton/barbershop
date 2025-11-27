package com.empresa.barberiasaas.clientes.application;

import com.empresa.barberiasaas.clientes.domain.LoyaltyAction;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoyaltyActionResponse(
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
    public static LoyaltyActionResponse from(LoyaltyAction action) {
        return new LoyaltyActionResponse(
                action.id(),
                action.clientId(),
                action.program(),
                action.reason(),
                action.points(),
                action.tier(),
                action.appliedAt(),
                action.reminderAt(),
                action.reminderMessage()
        );
    }
}
