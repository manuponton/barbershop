package com.empresa.barberiasaas.citas.infrastructure;

import com.empresa.barberiasaas.citas.application.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentRequest request) {
        var created = appointmentService.create(
                request.clientName(),
                request.barberName(),
                request.service(),
                request.startAt(),
                request.duration(),
                request.sucursalId()
        );
        return ResponseEntity.ok(AppointmentResponse.from(created));
    }

    @GetMapping
    public List<AppointmentResponse> list(@RequestParam(name = "from", required = false) LocalDateTime from) {
        var base = from != null ? from : LocalDateTime.now();
        return appointmentService.upcoming(base).stream()
                .map(AppointmentResponse::from)
                .toList();
    }
}
