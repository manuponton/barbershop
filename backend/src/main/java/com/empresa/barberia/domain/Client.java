package com.empresa.barberia.domain;

import java.time.LocalDate;

public record Client(
        String id,
        String name,
        String email,
        LocalDate birthday,
        String sucursalId,
        String lifecycleStatus,
        String loyaltyProgram,
        int loyaltyPoints,
        String loyaltyTier
) {
}
