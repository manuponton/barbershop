package com.empresa.barberia.api;

import com.empresa.barberia.api.dto.AppointmentResponse;
import com.empresa.barberia.api.dto.CreateAppointmentRequest;
import com.empresa.barberia.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/citas")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public Mono<AppointmentResponse> create(@Valid @RequestBody CreateAppointmentRequest request) {
        return appointmentService.createAppointment(
                        request.clientName(),
                        request.barberName(),
                        request.service(),
                        request.startAt(),
                        request.durationMinutes()
                )
                .map(AppointmentResponse::from);
    }

    @GetMapping
    public Flux<AppointmentResponse> list() {
        return appointmentService.listAppointments().map(AppointmentResponse::from);
    }
}
