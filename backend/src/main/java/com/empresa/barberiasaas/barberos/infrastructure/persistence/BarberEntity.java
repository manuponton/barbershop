package com.empresa.barberiasaas.barberos.infrastructure.persistence;

import com.empresa.barberiasaas.sucursales.infrastructure.persistence.SucursalEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "barbers")
public class BarberEntity {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    private String name;

    @Column(name = "services_csv")
    private String servicesCsv;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id")
    private SucursalEntity sucursal;

    public BarberEntity() {
    }

    public BarberEntity(UUID id, String name, String servicesCsv, SucursalEntity sucursal) {
        this.id = id;
        this.name = name;
        this.servicesCsv = servicesCsv;
        this.sucursal = sucursal;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getServicesCsv() {
        return servicesCsv;
    }

    public SucursalEntity getSucursal() {
        return sucursal;
    }
}

