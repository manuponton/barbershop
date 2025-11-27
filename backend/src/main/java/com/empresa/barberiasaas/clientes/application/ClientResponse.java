package com.empresa.barberiasaas.clientes.application;

import com.empresa.barberiasaas.clientes.domain.Client;

import java.time.LocalDate;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        String name,
        String email,
        LocalDate birthday,
        String lifecycleStatus,
        String loyaltyProgram,
        int loyaltyPoints,
        String loyaltyTier
) {
    public static ClientResponse from(Client client) {
        return new ClientResponse(
                client.id(),
                client.name(),
                client.email(),
                client.birthday(),
                client.lifecycleStatus().name(),
                client.loyaltyProfile().program(),
                client.loyaltyProfile().points(),
                client.loyaltyProfile().tier()
        );
    }
}
