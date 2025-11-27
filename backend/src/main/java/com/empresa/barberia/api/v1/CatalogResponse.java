package com.empresa.barberia.api.v1;

import java.util.List;

public record CatalogResponse(String sucursalId, List<CatalogItemResponse> items) {
}
