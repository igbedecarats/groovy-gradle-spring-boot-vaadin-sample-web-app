package app.locations.usecase

import app.locations.domain.Location
import app.locations.domain.LocationRepository

class LocationInteractor {

    private LocationRepository repository

    LocationInteractor(final LocationRepository repository) {
        this.repository = repository
    }

    Location findByName(final String locationName) {
        repository.findByName(locationName)
    }

    List<Location> findAll() {
        repository.findAll()
    }
}
