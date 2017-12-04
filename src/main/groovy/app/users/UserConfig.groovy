package app.users

import app.users.domain.UserRepository
import app.users.usecase.usecase.UserInteractor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfig {

    @Autowired
    private UserRepository userRepository

    @Bean
    UserInteractor userInteractor() {
        return new UserInteractor(userRepository)
    }

}
