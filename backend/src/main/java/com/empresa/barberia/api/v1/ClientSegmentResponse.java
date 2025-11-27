package com.empresa.barberia.api.v1;

public record ClientSegmentResponse(String id, String clientId, String segment, String criteria, String segmentedAt, String nextReminderAt, String reminderNote) {
}
