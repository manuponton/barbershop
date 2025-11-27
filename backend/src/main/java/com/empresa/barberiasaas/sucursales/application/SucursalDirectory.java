package com.empresa.barberiasaas.sucursales.application;

import com.empresa.barberiasaas.sucursales.domain.AvailabilityRule;
import com.empresa.barberiasaas.sucursales.domain.BrandingConfig;
import com.empresa.barberiasaas.sucursales.domain.CatalogItem;
import com.empresa.barberiasaas.sucursales.domain.Sucursal;
import com.empresa.barberiasaas.sucursales.infrastructure.persistence.AvailabilityRuleEntity;
import com.empresa.barberiasaas.sucursales.infrastructure.persistence.CatalogItemEntity;
import com.empresa.barberiasaas.sucursales.infrastructure.persistence.SucursalEntity;
import com.empresa.barberiasaas.sucursales.infrastructure.persistence.SucursalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SucursalDirectory {

    private final SucursalRepository sucursalRepository;

    public SucursalDirectory(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    public List<Sucursal> all() {
        return sucursalRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    public Optional<Sucursal> byId(UUID id) {
        return sucursalRepository.findById(id).map(this::toDomain);
    }

    public List<CatalogItem> catalog(UUID sucursalId) {
        return sucursalRepository.findById(sucursalId)
                .map(SucursalEntity::getCatalogItems)
                .stream()
                .flatMap(List::stream)
                .map(this::toDomain)
                .toList();
    }

    private Sucursal toDomain(SucursalEntity entity) {
        return new Sucursal(
                entity.getId(),
                entity.getNombre(),
                entity.getSlug(),
                entity.getDireccion(),
                entity.getCiudad(),
                new BrandingConfig(
                        entity.getBrandingPrimaryColor(),
                        entity.getBrandingHighlightColor(),
                        entity.getBrandingAccentColor(),
                        entity.getBrandingHeadline(),
                        entity.getBrandingTagline()
                ),
                entity.getAvailabilityRules().stream().map(this::toDomain).collect(Collectors.toList()),
                entity.getCatalogItems().stream().map(this::toDomain).collect(Collectors.toList())
        );
    }

    private AvailabilityRule toDomain(AvailabilityRuleEntity entity) {
        return new AvailabilityRule(entity.getDayOfWeek(), entity.getOpenTime(), entity.getCloseTime(), entity.isOnline());
    }

    private CatalogItem toDomain(CatalogItemEntity entity) {
        return new CatalogItem(
                entity.getId(),
                entity.getSucursal().getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getType(),
                entity.getPrice(),
                entity.getDurationMinutes()
        );
    }
}
