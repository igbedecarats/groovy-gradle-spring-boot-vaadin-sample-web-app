package app.contracts.domain

import app.feedbacks.domain.Feedback
import app.quotations.domain.Quotation
import app.services.domain.Service
import app.users.domain.User
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.Validate

import javax.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "contract")
@JsonIgnoreProperties(ignoreUnknown = true)
class Contract {

    @Id
    @GeneratedValue
    private long id

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

    @Column(name = "is_client_approved", nullable = false)
    private boolean isClientApproved

    @Column(name = "is_provider_approved", nullable = false)
    private boolean isProviderApproved

    @Enumerated(EnumType.STRING)
    private ContractStatus status

    @OneToMany()
    @JoinColumn(name = "quotation_id", referencedColumnName = "id")
    private List<Quotation> quotations = new ArrayList<>()

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "contract")
    private List<Feedback> feedbacks = new ArrayList<>()

    Contract(User client, Service service, LocalDateTime scheduledTime,
             List<Quotation> quotations) {
        this.client = client
        this.service = service
        this.scheduledTime = scheduledTime
        this.status = ContractStatus.IN_PROGRESS
        this.quotations = quotations
        this.creationTime = LocalDateTime.now()
        this.isClientApproved = false
        this.isProviderApproved = false
    }

    Contract() {

    }

    long getId() {
        id
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

    boolean getIsClientApproved() {
        isClientApproved
    }

    boolean getIsProviderApproved() {
        isProviderApproved
    }

    ContractStatus getStatus() {
        status
    }

    List<Quotation> getQuotations() {
        quotations
    }

    List<Feedback> getFeedbacks() {
        feedbacks
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

    void setIsClientApproved(final boolean isClientApproved) {
        this.isClientApproved = isClientApproved
    }

    void setIsProviderApproved(final boolean isProviderApproved) {
        this.isProviderApproved = isProviderApproved
    }

    void setStatus(final ContractStatus status) {
        this.status = status
    }

    void setQuotations(final List<Quotation> quotations) {
        this.quotations = quotations
    }

    void setFeedbacks(final List<Feedback> feedbacks) {
        this.feedbacks = feedbacks
    }

    void clientApproved() {
        if (!isClientApproved) {
            isClientApproved = true
            status = ContractStatus.DONE_BY_CLIENT
            if (isProviderApproved) {
                status = ContractStatus.COMPLETED
            }
        }
    }

    void providerApproved() {
        if (!isProviderApproved) {
            isProviderApproved = true
            status = ContractStatus.DONE_BY_PROVIDER
            if (isClientApproved) {
                status = ContractStatus.COMPLETED
            }
        }
    }

    void addFeedback(final Feedback feedback) {
        Validate.notNull(feedback, "The Feedback cannot be null")
        if (feedbackAlreadyGivenByUser(feedback.getSender())) {
            throw new IllegalArgumentException("Feedback already given!")
        }
        this.feedbacks.add(feedback)
    }

    boolean feedbackAlreadyGivenByUser(final User user) {
        Validate.notNull(user, "The User cannot be null")
        feedbacks.stream().anyMatch { feedback -> (((Feedback) feedback)["sender"] == user) }
    }

    boolean isCompleted() {
        ContractStatus.COMPLETED.equals(this.status)
    }

    boolean isClient(final User user) {
        user == contract.getClient()
    }

    boolean isProvider(final User user) {
        user == contract.getService().getProvider()
    }

    void markDoneByUser(final User user) {
        if (isClient(user)) {
            this.clientApproved()
        } else if (isProvider(user)) {
            this.providerApproved()
        } else {
            throw new IllegalArgumentException("User doesn't belong to contract")
        }
    }
}
