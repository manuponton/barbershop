package com.empresa.barberia.repository;

import com.empresa.barberia.domain.AvailabilityRule;
import com.empresa.barberia.domain.Branch;
import com.empresa.barberia.domain.Branding;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Repository
public class BranchRepository {

    private final List<Branch> branches = List.of(
            new Branch(
                    "central",
                    "Sucursal Centro",
                    "centro",
                    "Av. Principal 123",
                    "Ciudad A",
                    new Branding("#1f2937", "#111827", "#f59e0b", "Barbería Centro", "Cortes precisos, estilo urbano"),
                    List.of(
                            new AvailabilityRule("Lunes", "09:00", "19:00", true),
                            new AvailabilityRule("Martes", "09:00", "19:00", true),
                            new AvailabilityRule("Sábado", "10:00", "16:00", false)
                    )
            ),
            new Branch(
                    "norte",
                    "Sucursal Norte",
                    "norte",
                    "Calle Norte 45",
                    "Ciudad B",
                    new Branding("#0ea5e9", "#0b749c", "#f97316", "Barbería Norte", "Experiencias premium"),
                    List.of(
                            new AvailabilityRule("Miércoles", "10:00", "20:00", true),
                            new AvailabilityRule("Jueves", "10:00", "20:00", true),
                            new AvailabilityRule("Domingo", "11:00", "15:00", false)
                    )
            )
    );

    public Flux<Branch> findAll() {
        return Flux.fromIterable(branches);
    }
}
