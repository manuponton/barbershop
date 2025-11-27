package com.empresa.barberia.repository;

import com.empresa.barberia.domain.ServiceOffering;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Repository
public class ServiceCatalogRepository {

    private final List<ServiceOffering> services = List.of(
            new ServiceOffering(UUID.randomUUID().toString(), "Corte clásico", 30, 15.0, "central"),
            new ServiceOffering(UUID.randomUUID().toString(), "Barba premium", 25, 12.0, "central"),
            new ServiceOffering(UUID.randomUUID().toString(), "Color y diseño", 60, 40.0, "norte"),
            new ServiceOffering(UUID.randomUUID().toString(), "Perfilado", 20, 10.0, "norte")
    );

    public Flux<ServiceOffering> findAll() {
        return Flux.fromIterable(services);
    }
}
