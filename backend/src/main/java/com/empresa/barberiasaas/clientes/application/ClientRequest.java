package com.empresa.barberiasaas.clientes.application;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.UUID;

public record ClientRequest(
        @NotBlank String name,
        @Email String email,
        @NotNull @Past LocalDate birthday,
        @NotNull UUID sucursalId
) {
}
