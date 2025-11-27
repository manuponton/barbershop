package com.empresa.barberia.api.v1;

public record LoyaltyActionResponse(String id, String clientId, String program, String reason, int points, String tier, String appliedAt, String reminderAt, String reminderMessage) {
}
