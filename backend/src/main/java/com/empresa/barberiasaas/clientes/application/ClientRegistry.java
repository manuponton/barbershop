package com.empresa.barberiasaas.clientes.application;

import com.empresa.barberiasaas.clientes.domain.Client;
import com.empresa.barberiasaas.sucursales.application.SucursalDirectory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ClientRegistry {

    private final List<Client> clients = new ArrayList<>();
    private final SucursalDirectory sucursalDirectory;

    public ClientRegistry(SucursalDirectory sucursalDirectory) {
        this.sucursalDirectory = sucursalDirectory;
    }

    public Client register(@NotBlank String name, @Email String email, @Past LocalDate birthday, UUID sucursalId) {
        sucursalDirectory.byId(sucursalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sucursal no encontrada"));

        Client client = new Client(UUID.randomUUID(), sucursalId, name, email, birthday);
        clients.add(client);
        return client;
    }

    public List<Client> all() {
        return List.copyOf(clients);
    }
}
