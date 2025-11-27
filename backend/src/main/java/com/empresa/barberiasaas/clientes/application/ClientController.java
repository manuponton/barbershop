package com.empresa.barberiasaas.clientes.application;

import jakarta.validation.Valid;
import com.empresa.barberiasaas.clientes.domain.ReviewChannel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClientController {

    private final ClientRegistry registry;

    public ClientController(ClientRegistry registry) {
        this.registry = registry;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> register(@Valid @RequestBody ClientRequest request) {
        var created = registry.register(request.name(), request.email(), request.birthday(), request.sucursalId());
        return ResponseEntity.ok(ClientResponse.from(created));
    }

    @GetMapping
    public List<ClientResponse> list() {
        return registry.all().stream().map(ClientResponse::from).toList();
    }

    @PostMapping("/segmentos")
    public ResponseEntity<SegmentResponse> segmentar(@Valid @RequestBody SegmentRequest request) {
        var segment = registry.segment(request.clientId(), request.segmento(), request.criterio(), request.recordatorioPara(), request.nota());
        return ResponseEntity.ok(SegmentResponse.from(segment));
    }

    @GetMapping("/segmentos")
    public List<SegmentResponse> segmentos() {
        return registry.segments().stream().map(SegmentResponse::from).toList();
    }

    @PostMapping("/reseñas")
    public ResponseEntity<ReviewResponse> resenar(@Valid @RequestBody ReviewRequest request) {
        var channel = switch (request.channel().toLowerCase()) {
            case "redes" -> ReviewChannel.REDES;
            case "portal_web", "portal" -> ReviewChannel.PORTAL_WEB;
            default -> ReviewChannel.EN_SALON;
        };
        var review = registry.publishReview(request.clientId(), request.rating(), request.comment(), channel);
        return ResponseEntity.ok(ReviewResponse.from(review));
    }

    @GetMapping("/reseñas")
    public List<ReviewResponse> resenas() {
        return registry.reviews().stream().map(ReviewResponse::from).toList();
    }

    @PostMapping("/fidelizacion/acciones")
    public ResponseEntity<LoyaltyActionResponse> accion(@Valid @RequestBody LoyaltyActionRequest request) {
        var action = registry.applyLoyaltyAction(request.clientId(), request.points(), request.reason(), request.program(), request.sendReminder());
        return ResponseEntity.ok(LoyaltyActionResponse.from(action));
    }

    @GetMapping("/fidelizacion/acciones")
    public List<LoyaltyActionResponse> acciones() {
        return registry.loyaltyActions().stream().map(LoyaltyActionResponse::from).toList();
    }

    @GetMapping("/notificaciones")
    public List<NotificationResponse> notificaciones() {
        return registry.notifications().stream().map(NotificationResponse::from).toList();
    }
}
