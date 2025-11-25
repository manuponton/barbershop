package com.empresa.barberiasaas.clientes.application;

import com.empresa.barberiasaas.clientes.domain.Client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ClientRegistry {

    private final List<Client> clients = new ArrayList<>();

    public Client register(@NotBlank String name, @Email String email, @Past LocalDate birthday) {
        Client client = new Client(UUID.randomUUID(), name, email, birthday);
        clients.add(client);
        return client;
    }

    public List<Client> all() {
        return List.copyOf(clients);
    }
}
