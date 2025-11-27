package com.empresa.barberia.repository;

import com.empresa.barberia.domain.Client;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ClientRepository {

    private final Map<String, Client> clients = new ConcurrentHashMap<>();

    public Mono<Client> save(String name, String email, LocalDate birthday, String sucursalId) {
        var client = new Client(
                UUID.randomUUID().toString(),
                name,
                email,
                birthday,
                sucursalId != null ? sucursalId : "central",
                "ACTIVE",
                "BASE",
                0,
                "BRONCE"
        );
        clients.put(client.id(), client);
        return Mono.just(client);
    }

    public Flux<Client> findAll() {
        return Flux.fromIterable(clients.values())
                .sort((a, b) -> a.name().compareToIgnoreCase(b.name()));
    }

    public Optional<Client> findByName(String name) {
        return clients.values().stream()
                .filter(client -> client.name().equalsIgnoreCase(name))
                .findFirst();
    }

    public Flux<Client> findBySucursal(String sucursalId) {
        return Flux.fromIterable(clients.values())
                .filter(client -> sucursalId == null || client.sucursalId().equals(sucursalId))
                .sort((a, b) -> a.name().compareToIgnoreCase(b.name()));
    }
}
