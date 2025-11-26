package com.empresa.barberia.api.dto;

import com.empresa.barberia.domain.Client;

import java.time.LocalDate;

public record ClientResponse(String id, String name, String email, LocalDate birthday) {
    public static ClientResponse from(Client client) {
        return new ClientResponse(client.id(), client.name(), client.email(), client.birthday());
    }
}
