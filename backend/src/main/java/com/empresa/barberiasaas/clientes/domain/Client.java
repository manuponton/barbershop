package com.empresa.barberiasaas.clientes.domain;

import java.time.LocalDate;
import java.util.UUID;

public record Client(
        UUID id,
        String name,
        String email,
        LocalDate birthday,
        ClientLifecycleStatus lifecycleStatus,
        LoyaltyProfile loyaltyProfile
) {
    public Client withStatus(ClientLifecycleStatus status) {
        return new Client(id, name, email, birthday, status, loyaltyProfile);
    }

    public Client withLoyaltyProfile(LoyaltyProfile profile) {
        return new Client(id, name, email, birthday, lifecycleStatus, profile);
    }
}
