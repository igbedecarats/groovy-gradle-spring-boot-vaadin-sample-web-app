package app.feedbacks.usecase

import app.contracts.domain.Contract
import app.contracts.domain.ContractRepository
import app.feedbacks.domain.Feedback
import app.feedbacks.domain.FeedbackRepository
import app.services.domain.Service
import app.users.domain.User
import org.jsoup.helper.Validate
import org.springframework.util.CollectionUtils

class FeedbackInteractor {

    private FeedbackRepository feedbackRepository

    private ContractRepository contractRepository

    FeedbackInteractor(FeedbackRepository feedbackRepository,
                       ContractRepository contractRepository) {
        this.feedbackRepository = feedbackRepository
        this.contractRepository = contractRepository
    }

    Feedback submitByUser(User user, Contract contract, String comment, Integer rating) {
        User sender = user
        User recipient = getRecipient(contract, user)
        Feedback feedback = feedbackRepository.save(new Feedback(sender, recipient, contract, rating, comment))
        contract.addFeedback(feedback)
        contractRepository.save(contract)
        feedback
    }

    private User getRecipient(Contract contract, User user) {
        User recipient
        if (contract.isClient(user)) {
            recipient = contract.getService().getProvider()
        } else if (contract.isProvider(user)) {
            recipient = contract.getClient()
        } else {
            throw new IllegalArgumentException("The Users must belong to the Contract")
        }
        recipient
    }

    float calculateRatingFrom(final List<Feedback> feedbacks) {
        Validate.notNull(feedbacks, "The Feedbacks cannot be null")
        float rating = 0f
        feedbacks.forEach { feedback -> rating += ((Feedback) feedback).getRating() }
        if (!CollectionUtils.isEmpty(feedbacks)) {
            rating = ((float) rating / (float) feedbacks.size()) as float
        }
        rating
    }

    List<Feedback> getFeedbacksCreatedByClient(final User user) {
        Validate.notNull(user, "The User cannot be null")
        feedbackRepository.findByRecipientId(user.getId())
    }

    List<Feedback> getFeedbacksForServiceAndRecipient(final Service service, final User user) {
        Validate.notNull(service, "The Service cannot be null")
        Validate.notNull(user, "The User cannot be null")
        feedbackRepository.findByContractServiceIdAndRecipientId(service.getId(), user.getId())
    }
}
