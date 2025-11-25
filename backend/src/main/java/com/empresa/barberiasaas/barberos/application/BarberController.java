package com.empresa.barberiasaas.barberos.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/barberos")
public class BarberController {

    private final BarberCatalog catalog;

    public BarberController(BarberCatalog catalog) {
        this.catalog = catalog;
    }

    @GetMapping
    public List<BarberResponse> list() {
        return catalog.all().stream().map(BarberResponse::from).toList();
    }
}
