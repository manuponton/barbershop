package com.empresa.barberia.api;

import com.empresa.barberia.api.dto.BarberResponse;
import com.empresa.barberia.repository.BarberRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/barberos")
public class BarberController {

    private final BarberRepository barberRepository;

    public BarberController(BarberRepository barberRepository) {
        this.barberRepository = barberRepository;
    }

    @GetMapping
    public Flux<BarberResponse> list() {
        return barberRepository.findAll().map(BarberResponse::from);
    }
}
