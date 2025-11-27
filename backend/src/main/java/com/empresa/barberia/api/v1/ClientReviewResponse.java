package com.empresa.barberia.api.v1;

public record ClientReviewResponse(String id, String clientId, int rating, String comment, String channel, String createdAt, String followUpAt) {
}
