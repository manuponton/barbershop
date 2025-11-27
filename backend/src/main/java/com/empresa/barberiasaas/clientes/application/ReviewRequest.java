package com.empresa.barberiasaas.clientes.application;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReviewRequest(
        @NotNull UUID clientId,
        @Min(1) @Max(5) int rating,
        @NotBlank String comment,
        @NotBlank String channel
) {
}
