package com.empresa.barberia.api.v1;

import com.empresa.barberia.repository.ServiceCatalogRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/catalogo")
public class CatalogController {

    private final ServiceCatalogRepository serviceCatalogRepository;

    public CatalogController(ServiceCatalogRepository serviceCatalogRepository) {
        this.serviceCatalogRepository = serviceCatalogRepository;
    }

    @GetMapping
    public Flux<CatalogResponse> list() {
        return serviceCatalogRepository.findAll()
                .sort(Comparator.comparing(service -> service.sucursalId() + service.name()))
                .collect(Collectors.groupingBy(service -> service.sucursalId()))
                .flatMapMany(map -> Flux.fromIterable(map.entrySet()))
                .map(entry -> new CatalogResponse(entry.getKey(), entry.getValue().stream()
                        .map(service -> new CatalogItemResponse(service.id(), service.name(), "Servicio de barbería", "Servicio", service.price(), service.durationMinutes()))
                        .toList()));
    }
}
