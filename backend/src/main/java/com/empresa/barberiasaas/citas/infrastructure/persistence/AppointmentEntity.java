package com.empresa.barberiasaas.citas.infrastructure.persistence;

import com.empresa.barberiasaas.sucursales.infrastructure.persistence.SucursalEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class AppointmentEntity {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "barber_name")
    private String barberName;

    @Column(name = "service_name")
    private String serviceName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id")
    private SucursalEntity sucursal;

    public AppointmentEntity() {
    }

    public AppointmentEntity(UUID id, LocalDateTime startAt, int durationMinutes, String clientName, String barberName, String serviceName, SucursalEntity sucursal) {
        this.id = id;
        this.startAt = startAt;
        this.durationMinutes = durationMinutes;
        this.clientName = clientName;
        this.barberName = barberName;
        this.serviceName = serviceName;
        this.sucursal = sucursal;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getClientName() {
        return clientName;
    }

    public String getBarberName() {
        return barberName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public SucursalEntity getSucursal() {
        return sucursal;
    }
}

