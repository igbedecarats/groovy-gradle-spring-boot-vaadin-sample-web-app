package app.feedbacks.domain

import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByContractServiceIdAndRecipientId(long serviceId, long userId)

    List<Feedback> findByRecipientId(long userId)
}