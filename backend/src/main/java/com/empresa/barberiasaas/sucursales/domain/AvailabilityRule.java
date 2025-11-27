package com.empresa.barberiasaas.sucursales.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AvailabilityRule(DayOfWeek dayOfWeek, LocalTime opensAt, LocalTime closesAt, boolean onlineBookingEnabled) {

    public boolean allows(LocalDateTime start) {
        if (!onlineBookingEnabled) {
            return false;
        }
        if (!start.getDayOfWeek().equals(dayOfWeek)) {
            return false;
        }
        var time = start.toLocalTime();
        return !time.isBefore(opensAt) && !time.isAfter(closesAt);
    }
}
