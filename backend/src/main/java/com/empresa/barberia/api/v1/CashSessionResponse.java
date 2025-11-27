package com.empresa.barberia.api.v1;

import com.empresa.barberia.domain.CashSession;

import java.time.LocalDateTime;

public record CashSessionResponse(String id, LocalDateTime openedAt, LocalDateTime closedAt, double openingAmount, Double closingAmount) {
    public static CashSessionResponse from(CashSession session) {
        return new CashSessionResponse(session.id(), session.openedAt(), session.closedAt(), session.openingAmount(), session.closingAmount());
    }
}
