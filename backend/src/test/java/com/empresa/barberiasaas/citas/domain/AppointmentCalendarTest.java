package com.empresa.barberiasaas.citas.domain;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentCalendarTest {

    @Test
    void preventsOverlapForSameBarber() {
        var calendar = new AppointmentCalendar();
        var barberId = UUID.randomUUID();
        var first = new AppointmentDetails(UUID.randomUUID(), barberId, UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now(), Duration.ofMinutes(30), AppointmentStatus.BOOKED);
        calendar.schedule(first);

        var overlapping = new AppointmentDetails(UUID.randomUUID(), barberId, UUID.randomUUID(), UUID.randomUUID(),
                first.startAt().plusMinutes(15), Duration.ofMinutes(30), AppointmentStatus.CONFIRMED);

        assertThrows(IllegalArgumentException.class, () -> calendar.schedule(overlapping));
    }

    @Test
    void allowsSchedulingWhenPreviousWasCancelled() {
        var calendar = new AppointmentCalendar();
        var barberId = UUID.randomUUID();
        var start = LocalDateTime.now();
        calendar.schedule(new AppointmentDetails(UUID.randomUUID(), barberId, UUID.randomUUID(), UUID.randomUUID(),
                start, Duration.ofMinutes(30), AppointmentStatus.CANCELLED));

        var newAppointment = new AppointmentDetails(UUID.randomUUID(), barberId, UUID.randomUUID(), UUID.randomUUID(),
                start, Duration.ofMinutes(30), AppointmentStatus.BOOKED);

        assertDoesNotThrow(() -> calendar.schedule(newAppointment));
    }
}
