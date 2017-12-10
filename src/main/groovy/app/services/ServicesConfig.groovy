package app.services

import app.feedbacks.domain.FeedbackRepository
import app.locations.usecase.LocationInteractor
import app.services.domain.ServiceCategoryRepository
import app.services.domain.ServiceRepository
import app.services.domain.ServiceSubCategoryRepository
import app.services.usecase.ServiceInteractor
import app.users.domain.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServicesConfig {

    @Autowired
    private ServiceRepository serviceRepository

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository

    @Autowired
    private ServiceSubCategoryRepository serviceSubCategoryRepository

    @Autowired
    private UserRepository userRepository

    @Autowired
    private LocationInteractor locationInteractor

    @Autowired
    private FeedbackRepository feedbackRepository

    @Bean
    ServiceInteractor serviceInteractor() {
        new ServiceInteractor(serviceRepository, serviceCategoryRepository,
                serviceSubCategoryRepository, userRepository, locationInteractor, feedbackRepository)
    }
}
