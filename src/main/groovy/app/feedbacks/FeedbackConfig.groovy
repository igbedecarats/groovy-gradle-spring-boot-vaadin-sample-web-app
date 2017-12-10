package app.feedbacks

import app.contracts.domain.ContractRepository
import app.feedbacks.domain.FeedbackRepository
import app.feedbacks.usecase.FeedbackInteractor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeedbackConfig {

    @Autowired
    private FeedbackRepository feedbackRepository

    @Autowired
    private ContractRepository contractRepository

    @Bean
    FeedbackInteractor feedbackInteractor() {
        return new FeedbackInteractor(feedbackRepository, contractRepository)
    }
}
