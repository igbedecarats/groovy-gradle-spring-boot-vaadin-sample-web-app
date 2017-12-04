package app.global.security

import app.users.domain.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.vaadin.spring.security.config.AuthenticationManagerConfigurer

@Configuration
class SecurityConfiguration implements AuthenticationManagerConfigurer {

    @Autowired
    private UserRepository userRepository

    @Override
    void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new UserAuthenticationProvider(userRepository))
    }
}
