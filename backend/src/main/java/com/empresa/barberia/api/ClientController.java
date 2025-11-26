package com.empresa.barberia.api;

import com.empresa.barberia.api.dto.ClientResponse;
import com.empresa.barberia.api.dto.CreateClientRequest;
import com.empresa.barberia.repository.ClientRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/clientes")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping
    public Mono<ClientResponse> create(@Valid @RequestBody CreateClientRequest request) {
        return clientRepository.save(request.name(), request.email(), request.birthday())
                .map(ClientResponse::from);
    }

    @GetMapping
    public Flux<ClientResponse> list() {
        return clientRepository.findAll().map(ClientResponse::from);
    }
}
