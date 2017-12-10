package app.quotations.domain

import app.services.domain.Service
import app.users.domain.User
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import java.time.LocalDateTime

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

    Quotation(String description, User client, Service service,
                     LocalDateTime scheduledTime) {
        this.description = description
        this.client = client
        this.service = service
        this.scheduledTime = scheduledTime
        this.creationTime = LocalDateTime.now()
        this.status = QuotationStatus.CREATED
    }

    boolean isCreated() {
        status.equals(QuotationStatus.CREATED)
    }
    
    Quotation() { }

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
