package com.empresa.barberiasaas.barberos.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BarberRepository extends JpaRepository<BarberEntity, UUID> {
}

