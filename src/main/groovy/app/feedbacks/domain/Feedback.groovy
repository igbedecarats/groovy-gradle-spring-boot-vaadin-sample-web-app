package app.feedbacks.domain

import app.contracts.domain.Contract
import app.users.domain.User
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "feedback")
@JsonIgnoreProperties(ignoreUnknown = true)
class Feedback {

    @Id
    @GeneratedValue
    private long id

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id")
    private User sender

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_id")
    private User recipient

    @ManyToOne(optional = false)
    @JoinColumn(name = "contract_id")
    private Contract contract

    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime

    @Column(name = "rating", nullable = false)
    private int rating

    @Column(name = "comment", nullable = false)
    private String comment

    static List<Integer> allowedRatings() {
        Arrays.asList(1, 2, 3, 4, 5)
    }

    Feedback(User sender, User recipient, Contract contract, int rating, String comment) {
        this.sender = sender
        this.recipient = recipient
        this.contract = contract
        this.creationTime = LocalDateTime.now()
        this.rating = rating
        this.comment = comment
    }

    long getId() {
        id
    }

    User getSender() {
        sender
    }

    User getRecipient() {
        recipient
    }

    Contract getContract() {
        contract
    }

    LocalDateTime getCreationTime() {
        creationTime
    }

    int getRating() {
        rating
    }

    String getComment() {
        comment
    }

    void setSender(final User sender) {
        this.sender = sender
    }

    void setRecipient(final User recipient) {
        this.recipient = recipient
    }

    void setContract(final Contract contract) {
        this.contract = contract
    }

    void setCreationTime(final LocalDateTime creationTime) {
        this.creationTime = creationTime
    }

    void setRating(final int rating) {
        this.rating = rating
    }

    void setComment(final String comment) {
        this.comment = comment
    }
}
