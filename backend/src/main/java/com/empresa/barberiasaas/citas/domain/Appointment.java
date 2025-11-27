package com.empresa.barberiasaas.citas.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Appointment {

    private final UUID id;
    private final LocalDateTime startAt;
    private final Duration duration;
    private final String clientName;
    private final String barberName;
    private final String service;
    private final UUID sucursalId;

    public Appointment(UUID id, LocalDateTime startAt, Duration duration, String clientName, String barberName, String service, UUID sucursalId) {
        this.id = Objects.requireNonNull(id, "id");
        this.startAt = Objects.requireNonNull(startAt, "startAt");
        this.duration = Objects.requireNonNull(duration, "duration");
        this.clientName = Objects.requireNonNull(clientName, "clientName");
        this.barberName = Objects.requireNonNull(barberName, "barberName");
        this.service = Objects.requireNonNull(service, "service");
        this.sucursalId = Objects.requireNonNull(sucursalId, "sucursalId");
    }

    public UUID id() {
        return id;
    }

    public LocalDateTime startAt() {
        return startAt;
    }

    public Duration duration() {
        return duration;
    }

    public String clientName() {
        return clientName;
    }

    public String barberName() {
        return barberName;
    }

    public String service() {
        return service;
    }

    public UUID sucursalId() {
        return sucursalId;
    }
}
