package com.empresa.barberiasaas.clientes.application;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LoyaltyActionRequest(
        @NotNull UUID clientId,
        int points,
        String reason,
        String program,
        boolean sendReminder
) {
}
