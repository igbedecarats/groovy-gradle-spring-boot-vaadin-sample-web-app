package app.users

import app.feedbacks.domain.FeedbackRepository
import app.feedbacks.usecase.FeedbackInteractor
import app.users.domain.UserRepository
import app.users.usecase.UserInteractor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfig {

    @Autowired
    private UserRepository userRepository

    @Autowired
    private FeedbackInteractor feedbackInteractor

    @Bean
    UserInteractor userInteractor() {
        return new UserInteractor(userRepository, feedbackInteractor)
    }

}
