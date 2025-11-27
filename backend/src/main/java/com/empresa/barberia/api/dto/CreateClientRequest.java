package com.empresa.barberia.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CreateClientRequest(
        @NotBlank(message = "El nombre es requerido") String name,
        @Email(message = "Email inválido") @NotBlank(message = "El email es requerido") String email,
        @NotNull(message = "La fecha de cumpleaños es requerida")
        @Past(message = "La fecha debe estar en el pasado")
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthday,
        String sucursalId
) {
}
