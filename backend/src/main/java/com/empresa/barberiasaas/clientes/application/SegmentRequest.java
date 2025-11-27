package com.empresa.barberiasaas.clientes.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record SegmentRequest(
        @NotNull UUID clientId,
        @NotBlank String segmento,
        String criterio,
        LocalDateTime recordatorioPara,
        String nota
) {
}
