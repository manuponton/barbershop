package com.empresa.barberiasaas.sucursales.infrastructure;

import com.empresa.barberiasaas.sucursales.application.SucursalDirectory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sucursales")
public class SucursalController {

    private final SucursalDirectory directory;

    public SucursalController(SucursalDirectory directory) {
        this.directory = directory;
    }

    @GetMapping
    public List<SucursalResponse> list() {
        return directory.all().stream().map(SucursalResponse::from).toList();
    }
}
