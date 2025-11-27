package com.empresa.barberiasaas.sucursales.infrastructure;

import com.empresa.barberiasaas.sucursales.application.SucursalDirectory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalogo")
public class CatalogoController {

    private final SucursalDirectory directory;

    public CatalogoController(SucursalDirectory directory) {
        this.directory = directory;
    }

    @GetMapping
    public List<CatalogoResponse> catalogo(@RequestParam(name = "sucursalId", required = false) UUID sucursalId) {
        if (sucursalId != null) {
            return List.of(CatalogoResponse.from(sucursalId, directory.catalog(sucursalId)));
        }
        return directory.all().stream()
                .map(sucursal -> CatalogoResponse.from(sucursal.id(), sucursal.catalog()))
                .toList();
    }
}
