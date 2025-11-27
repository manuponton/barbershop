package com.empresa.barberia.api.v1;

public record ClientNotificationResponse(String id, String clientId, String category, String message, String scheduledAt, boolean delivered) {
}
