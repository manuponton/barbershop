package com.empresa.barberiasaas.sucursales.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "catalog_items")
public class CatalogItemEntity {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    private String name;
    private String description;
    private String type;
    private BigDecimal price;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id")
    private SucursalEntity sucursal;

    public CatalogItemEntity() {
    }

    public CatalogItemEntity(UUID id, String name, String description, String type, BigDecimal price, Integer durationMinutes, SucursalEntity sucursal) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.sucursal = sucursal;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public SucursalEntity getSucursal() {
        return sucursal;
    }
}

