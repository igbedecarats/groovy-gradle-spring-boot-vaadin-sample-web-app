package app.feedbacks.usecase

import app.contracts.domain.Contract
import app.contracts.domain.ContractRepository
import app.feedbacks.domain.Feedback
import app.feedbacks.domain.FeedbackRepository
import app.users.domain.User

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
        User recipient
        if (user.equals(contract.getClient())) {
            recipient = contract.getService().getProvider()
        } else if (user.equals(contract.getService().getProvider())) {
            recipient = contract.getClient()
        } else {
            throw new IllegalArgumentException("The Users must belong to the Contract")
        }
        Feedback feedback = feedbackRepository.save(new Feedback(sender,recipient, contract, rating, comment))
        contract.addFeedback(feedback)
        contractRepository.save(contract)
        feedback
    }
}
