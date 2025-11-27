package com.empresa.barberiasaas.clientes.application;

import com.empresa.barberiasaas.clientes.domain.AccionDeFidelizacionAplicada;
import com.empresa.barberiasaas.clientes.domain.Client;
import com.empresa.barberiasaas.sucursales.application.SucursalDirectory;
import com.empresa.barberiasaas.clientes.domain.ClientLifecycleStatus;
import com.empresa.barberiasaas.clientes.domain.ClientNotification;
import com.empresa.barberiasaas.clientes.domain.ClientReview;
import com.empresa.barberiasaas.clientes.domain.ClientSegment;
import com.empresa.barberiasaas.clientes.domain.ClienteSegmentado;
import com.empresa.barberiasaas.clientes.domain.DomainEvent;
import com.empresa.barberiasaas.clientes.domain.LoyaltyAction;
import com.empresa.barberiasaas.clientes.domain.LoyaltyProfile;
import com.empresa.barberiasaas.clientes.domain.ResenaPublicada;
import com.empresa.barberiasaas.clientes.domain.ReviewChannel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ClientRegistry {

    private final List<Client> clients = new ArrayList<>();
    private final List<ClientSegment> segments = new ArrayList<>();
    private final List<ClientReview> reviews = new ArrayList<>();
    private final List<LoyaltyAction> loyaltyActions = new ArrayList<>();
    private final List<ClientNotification> notifications = new ArrayList<>();
    private final List<DomainEvent> events = new ArrayList<>();
    private final SucursalDirectory sucursalDirectory;

    public ClientRegistry(SucursalDirectory sucursalDirectory) {
        this.sucursalDirectory = sucursalDirectory;
    }

    public Client register(@NotBlank String name, @Email String email, @Past LocalDate birthday, UUID sucursalId) {
        sucursalDirectory.byId(sucursalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sucursal no encontrada"));

        Client client = new Client(
                UUID.randomUUID(),
                name,
                email,
                birthday,
                ClientLifecycleStatus.PROSPECTO,
                new LoyaltyProfile("Puntos Barbería", 0, "Base", LocalDate.now(), LocalDate.now().plusMonths(1), true)
        );
        clients.add(client);
        return client;
    }

    public List<Client> all() {
        return List.copyOf(clients);
    }

    public ClientSegment segment(@NotNull UUID clientId, @NotBlank String segmentName, String criteria, LocalDateTime reminderAt, String reminderNote) {
        Client client = clientById(clientId);
        ClientSegment assignment = new ClientSegment(UUID.randomUUID(), client.id(), segmentName, criteria, LocalDateTime.now(), reminderAt, reminderNote);
        segments.add(assignment);

        ClientLifecycleStatus status = ClientLifecycleStatus.ACTIVO;
        if (segmentName.toLowerCase().contains("vip")) {
            status = ClientLifecycleStatus.VIP;
        } else if (segmentName.toLowerCase().contains("riesgo")) {
            status = ClientLifecycleStatus.EN_RIESGO;
        }
        updateClient(client.withStatus(status));

        events.add(new ClienteSegmentado(client.id(), segmentName, criteria, LocalDateTime.now()));

        if (reminderAt != null) {
            notifications.add(new ClientNotification(UUID.randomUUID(), client.id(), "segmentacion", reminderNote != null ? reminderNote : "Seguimiento de segmento", reminderAt, false));
        }

        return assignment;
    }

    public List<ClientSegment> segments() {
        return List.copyOf(segments);
    }

    public ClientReview publishReview(@NotNull UUID clientId, int rating, @NotBlank String comment, ReviewChannel channel) {
        Client client = clientById(clientId);
        ClientLifecycleStatus status = rating <= 3 ? ClientLifecycleStatus.EN_RIESGO : ClientLifecycleStatus.RECURRENTE;
        updateClient(client.withStatus(status));

        ClientReview review = new ClientReview(UUID.randomUUID(), client.id(), rating, comment, channel, LocalDateTime.now(), LocalDateTime.now().plusDays(3));
        reviews.add(review);
        events.add(new ResenaPublicada(client.id(), rating, channel.name(), LocalDateTime.now()));

        notifications.add(new ClientNotification(UUID.randomUUID(), client.id(), "seguimiento", "Agendar seguimiento tras reseña: " + comment, review.followUpAt(), false));
        return review;
    }

    public List<ClientReview> reviews() {
        return List.copyOf(reviews);
    }

    public LoyaltyAction applyLoyaltyAction(@NotNull UUID clientId, int points, String reason, String program, boolean sendReminder) {
        Client client = clientById(clientId);
        LoyaltyProfile updatedProfile = client.loyaltyProfile().ensureProgram(program != null ? program : client.loyaltyProfile().program())
                .addPoints(points, LocalDate.now().plusMonths(1));

        String tier = updatedProfile.points() >= 200 ? "Elite" : updatedProfile.points() >= 100 ? "Gold" : updatedProfile.tier();
        updatedProfile = updatedProfile.withTier(tier);

        Client updatedClient = client.withLoyaltyProfile(updatedProfile);
        updateClient(updatedClient);

        LoyaltyAction action = new LoyaltyAction(UUID.randomUUID(), client.id(), updatedProfile.program(), reason, points, tier, LocalDateTime.now(), null, null);

        if (sendReminder) {
            LocalDateTime reminderAt = LocalDateTime.now().plusWeeks(2);
            action = new LoyaltyAction(action.id(), action.clientId(), action.program(), action.reason(), action.points(), action.tier(), action.appliedAt(), reminderAt, "Recordar canje de puntos");
            notifications.add(new ClientNotification(UUID.randomUUID(), client.id(), "fidelizacion", "Recordar canje de puntos y beneficios " + tier, reminderAt, false));
        }

        loyaltyActions.add(action);
        events.add(new AccionDeFidelizacionAplicada(client.id(), updatedProfile.program(), points, LocalDateTime.now()));
        return action;
    }

    public List<LoyaltyAction> loyaltyActions() {
        return List.copyOf(loyaltyActions);
    }

    public List<ClientNotification> notifications() {
        return List.copyOf(notifications);
    }

    public List<DomainEvent> events() {
        return List.copyOf(events);
    }

    private Client clientById(UUID clientId) {
        return clients.stream().filter(c -> c.id().equals(clientId)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    private void updateClient(Client updated) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).id().equals(updated.id())) {
                clients.set(i, updated);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado para actualizar");
    }
}
