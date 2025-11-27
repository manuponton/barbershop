package com.empresa.barberia.api.v1;

import com.empresa.barberia.api.dto.ClientResponse;
import com.empresa.barberia.api.dto.CreateClientRequest;
import com.empresa.barberia.repository.ClientRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClientInsightsController {

    private final ClientRepository clientRepository;
    private final List<ClientSegmentResponse> segments = new ArrayList<>();
    private final List<ClientReviewResponse> reviews = new ArrayList<>();
    private final List<LoyaltyActionResponse> loyaltyActions = new ArrayList<>();
    private final List<ClientNotificationResponse> notifications = new ArrayList<>();

    public ClientInsightsController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostConstruct
    void seed() {
        if (segments.isEmpty()) {
            var demoClient = new ClientResponse("seed", "Cliente demo", "demo@example.com", LocalDate.now().minusYears(20), "central", "ACTIVE", "BASE", 50, "PLATA");
            segments.add(new ClientSegmentResponse(UUID.randomUUID().toString(), demoClient.id(), "Frecuente", "3 visitas/mes", LocalDateTime.now().minusDays(5).toString(), LocalDateTime.now().plusDays(2).toString(), "Enviar recordatorio"));
            reviews.add(new ClientReviewResponse(UUID.randomUUID().toString(), demoClient.id(), 5, "Excelente servicio", "WEB", LocalDateTime.now().minusDays(1).toString(), null));
            loyaltyActions.add(new LoyaltyActionResponse(UUID.randomUUID().toString(), demoClient.id(), "BASE", "Registro", 10, "BRONCE", LocalDateTime.now().toString(), null, null));
            notifications.add(new ClientNotificationResponse(UUID.randomUUID().toString(), demoClient.id(), "REMINDER", "Visita programada", LocalDateTime.now().plusDays(1).toString(), false));
        }
    }

    @PostMapping
    public Mono<ClientResponse> create(@Valid @RequestBody CreateClientRequest request) {
        return clientRepository.save(request.name(), request.email(), request.birthday(), request.sucursalId())
                .map(ClientResponse::from);
    }

    @GetMapping
    public Flux<ClientResponse> list() {
        return clientRepository.findAll().map(ClientResponse::from);
    }

    @GetMapping("/segmentos")
    public Flux<ClientSegmentResponse> listSegments() {
        return Flux.fromIterable(segments);
    }

    @PostMapping("/segmentos")
    public Mono<ClientSegmentResponse> createSegment(@RequestBody ClientSegmentResponse payload) {
        var enriched = payload.nextReminderAt() == null ? new ClientSegmentResponse(UUID.randomUUID().toString(), payload.clientId(), payload.segment(), payload.criteria(), payload.segmentedAt(), LocalDateTime.now().plusDays(3).toString(), payload.reminderNote()) : payload;
        segments.add(enriched);
        return Mono.just(enriched);
    }

    @GetMapping("/reseñas")
    public Flux<ClientReviewResponse> listReviews() {
        return Flux.fromIterable(reviews);
    }

    @PostMapping("/reseñas")
    public Mono<ClientReviewResponse> createReview(@RequestBody ClientReviewResponse payload) {
        var enriched = new ClientReviewResponse(UUID.randomUUID().toString(), payload.clientId(), payload.rating(), payload.comment(), payload.channel(), payload.createdAt(), payload.followUpAt());
        reviews.add(enriched);
        return Mono.just(enriched);
    }

    @GetMapping("/fidelizacion/acciones")
    public Flux<LoyaltyActionResponse> listLoyalty() {
        return Flux.fromIterable(loyaltyActions);
    }

    @PostMapping("/fidelizacion/acciones")
    public Mono<LoyaltyActionResponse> createLoyalty(@RequestBody LoyaltyActionResponse payload) {
        var action = new LoyaltyActionResponse(UUID.randomUUID().toString(), payload.clientId(), payload.program(), payload.reason(), payload.points(), payload.tier(), payload.appliedAt(), payload.reminderAt(), payload.reminderMessage());
        loyaltyActions.add(action);
        return Mono.just(action);
    }

    @GetMapping("/notificaciones")
    public Flux<ClientNotificationResponse> listNotifications() {
        return Flux.fromIterable(notifications);
    }

    @PostMapping("/notificaciones")
    public Mono<ClientNotificationResponse> createNotification(@RequestBody ClientNotificationResponse payload) {
        var notification = new ClientNotificationResponse(UUID.randomUUID().toString(), payload.clientId(), payload.category(), payload.message(), payload.scheduledAt(), payload.delivered());
        notifications.add(notification);
        return Mono.just(notification);
    }
}
