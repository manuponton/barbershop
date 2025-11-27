package com.empresa.barberiasaas.sucursales.application;

import com.empresa.barberiasaas.sucursales.domain.AvailabilityRule;
import com.empresa.barberiasaas.sucursales.domain.BrandingConfig;
import com.empresa.barberiasaas.sucursales.domain.CatalogItem;
import com.empresa.barberiasaas.sucursales.domain.Sucursal;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SucursalDirectory {

    private final List<Sucursal> sucursales = new ArrayList<>();

    @PostConstruct
    void seed() {
        if (!sucursales.isEmpty()) {
            return;
        }

        var centroId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var norteId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        sucursales.add(new Sucursal(
                centroId,
                "Barbería Centro",
                "centro",
                "Cra 10 #20-30",
                "Ciudad",
                new BrandingConfig("#0f172a", "#e11d48", "#10b981", "Barbería Centro", "Cortes impecables en el corazón de la ciudad"),
                availabilityRules(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                        LocalTime.of(9, 0), LocalTime.of(19, 0), true),
                catalogForBranch(centroId)
        ));

        sucursales.add(new Sucursal(
                norteId,
                "Barbería Norte",
                "norte",
                "Av 5 #120-10",
                "Ciudad",
                new BrandingConfig("#111827", "#8b5cf6", "#f59e0b", "Barbería Norte", "Experiencias premium y spa"),
                availabilityRules(List.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY),
                        LocalTime.of(10, 0), LocalTime.of(21, 0), true),
                catalogForBranch(norteId)
        ));
    }

    public List<Sucursal> all() {
        return List.copyOf(sucursales);
    }

    public Optional<Sucursal> byId(UUID id) {
        return sucursales.stream().filter(s -> s.id().equals(id)).findFirst();
    }

    public List<CatalogItem> catalog(UUID sucursalId) {
        return byId(sucursalId).map(Sucursal::catalog).orElse(List.of());
    }

    private List<AvailabilityRule> availabilityRules(List<DayOfWeek> days, LocalTime open, LocalTime close, boolean online) {
        return days.stream().map(day -> new AvailabilityRule(day, open, close, online)).toList();
    }

    private List<CatalogItem> catalogForBranch(UUID branchId) {
        return List.of(
                new CatalogItem(UUID.randomUUID(), branchId, "Corte clásico", "Corte y estilo con toalla caliente", "servicio", BigDecimal.valueOf(35000), 40),
                new CatalogItem(UUID.randomUUID(), branchId, "Afeitado de lujo", "Ritual de afeitado con aceites esenciales", "servicio", BigDecimal.valueOf(42000), 35),
                new CatalogItem(UUID.randomUUID(), branchId, "Kit de cuidado de barba", "Incluye aceite y cepillo de barba", "producto", BigDecimal.valueOf(52000), null)
        );
    }
}
