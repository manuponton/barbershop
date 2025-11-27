package com.empresa.barberiasaas.sucursales.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SucursalRepository extends JpaRepository<SucursalEntity, UUID> {
}

