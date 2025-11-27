package com.empresa.barberiasaas.clientes.application;

import com.empresa.barberiasaas.clientes.domain.ClientNotification;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID clientId,
        String category,
        String message,
        LocalDateTime scheduledAt,
        boolean delivered
) {
    public static NotificationResponse from(ClientNotification notification) {
        return new NotificationResponse(
                notification.id(),
                notification.clientId(),
                notification.category(),
                notification.message(),
                notification.scheduledAt(),
                notification.delivered()
        );
    }
}
