package app.quotations.domain

import app.services.domain.Service
import app.users.domain.User
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.Validate

import javax.persistence.*
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(name = "quotation")
@JsonIgnoreProperties(ignoreUnknown = true)
class Quotation {

    @Id
    @GeneratedValue
    private long id

    @Column(name = "description", nullable = false)
    private String description

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private User client

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id")
    private Service service

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime

    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime

    @Enumerated(EnumType.STRING)
    private QuotationStatus status

    Quotation(final String description, final User client, final Service service, final LocalDateTime scheduledTime) {
        Validate.notBlank(description, "Por favor ingrese un comentario")
        validateScheduledTime(scheduledTime, service)
        this.description = description
        this.client = client
        this.service = service
        this.scheduledTime = scheduledTime
        this.creationTime = LocalDateTime.now()
        this.status = QuotationStatus.CREATED
    }

    private void validateScheduledTime(LocalDateTime scheduledTime, Service service) {
        if (LocalDateTime.now().compareTo(scheduledTime) > 0 ) {
            throw new IllegalArgumentException("No se pueden crear pedidos de contratación con fecha en el pasado")
        }
        if (scheduledTime.getDayOfWeek().getValue() < service.getStartDay()
                || scheduledTime.getDayOfWeek().getValue() > service.getEndDay()) {
            throw new IllegalArgumentException("El día del pedido de contratación debe estar contenido en los días " +
                    "que se presta el servicio")
        }

        LocalTime scheduledLocalTime = scheduledTime.toLocalTime()
        if (service.getLocalStartTime().compareTo(scheduledLocalTime) > 0
                || service.getLocalEndTime().compareTo(scheduledLocalTime) < 0) {
            throw new IllegalArgumentException("La hora del día del pedido de contratación debe estar contenido en " +
                    "el horario en el que se presta el servicio")
        }
    }

    boolean isCreated() {
        status.equals(QuotationStatus.CREATED)
    }

    Quotation() {}

    long getId() {
        id
    }

    String getDescription() {
        description
    }

    User getClient() {
        client
    }

    Service getService() {
        service
    }

    LocalDateTime getScheduledTime() {
        scheduledTime
    }

    LocalDateTime getCreationTime() {
        creationTime
    }

    QuotationStatus getStatus() {
        status
    }

    void setDescription(final String description) {
        this.description = description
    }

    void setClient(final User client) {
        this.client = client
    }

    void setService(final Service service) {
        this.service = service
    }

    void setScheduledTime(final LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime
    }

    void setCreationTime(final LocalDateTime creationTime) {
        this.creationTime = creationTime
    }

    void setStatus(final QuotationStatus status) {
        this.status = status
    }
}
