package app.locations

import app.locations.domain.LocationRepository
import app.locations.usecase.LocationInteractor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LocationConfig {

    @Autowired
    private LocationRepository locationRepository

    @Bean
    LocationInteractor locationInteractor() {
        return new LocationInteractor(locationRepository)
    }
}
