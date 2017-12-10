package app.locations

import app.locations.domain.LocationRepository
import app.locations.usecase.LocationInteractor
import com.google.maps.GeoApiContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LocationConfig {

    @Value('${google.maps.key}')
    private String apiKey

    @Autowired
    private LocationRepository locationRepository

    @Bean
    GeoApiContext geoApiContext() {
        new GeoApiContext().setApiKey(apiKey)
    }

    @Bean
    LocationInteractor locationInteractor() {
        new LocationInteractor(locationRepository, geoApiContext())
    }

}
