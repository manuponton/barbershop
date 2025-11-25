package com.empresa.barberiasaas.clientes.application;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClientController {

    private final ClientRegistry registry;

    public ClientController(ClientRegistry registry) {
        this.registry = registry;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> register(@Valid @RequestBody ClientRequest request) {
        var created = registry.register(request.name(), request.email(), request.birthday());
        return ResponseEntity.ok(ClientResponse.from(created));
    }

    @GetMapping
    public List<ClientResponse> list() {
        return registry.all().stream().map(ClientResponse::from).toList();
    }
}
