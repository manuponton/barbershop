package com.empresa.barberiasaas.clientes.application;

import com.empresa.barberiasaas.clientes.domain.Client;

import java.time.LocalDate;
import java.util.UUID;

public record ClientResponse(UUID id, UUID sucursalId, String name, String email, LocalDate birthday) {
    public static ClientResponse from(Client client) {
        return new ClientResponse(client.id(), client.sucursalId(), client.name(), client.email(), client.birthday());
    }
}
