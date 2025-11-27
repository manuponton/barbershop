package com.empresa.barberiasaas.clientes.domain;

import java.time.LocalDateTime;

public interface DomainEvent {
    String type();

    LocalDateTime occurredOn();
}
