package app.locations.usecase

import app.locations.domain.Coordinate
import app.locations.domain.Location
import app.locations.domain.LocationRepository
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.errors.ApiException
import com.google.maps.model.AddressComponent
import com.google.maps.model.AddressType
import com.google.maps.model.GeocodingResult

class LocationInteractor {

    private LocationRepository repository

    private GeoApiContext geoApiContext

    LocationInteractor(final LocationRepository repository, final GeoApiContext geoApiContext) {
        this.repository = repository
        this.geoApiContext = geoApiContext
    }

    List<Location> findAll() {
        repository.findAll()
    }

    Location findByName(final String locationName) {
        repository.findByName(locationName)
    }

    List<Location> findWithNameLike(final String locationName) {
        repository.findByNameIgnoreCaseContaining(locationName)
    }

    Location create(final CreateLocationRequest request) {
        Coordinate coordinateForLocation
        try {
            coordinateForLocation = findCoordinateForLocation(request.getName())

        } catch (Exception e) {
            coordinateForLocation = new Coordinate()
        }
        repository.save(new Location(request.getName(), request.getArea(),
                coordinateForLocation))
    }

    private Coordinate findCoordinateForLocation(final String name) {

        List<GeocodingResult> googleLocations = this.findGoogleLocations(name);
        GeocodingResult geocodingResult = googleLocations.stream().filter { isValidGeoCodingResult(it) }
                .findFirst()
                .orElseThrow { -> new IllegalArgumentException("Location couldn't be found.") }
        return new Coordinate(geocodingResult.geometry.location.lat, geocodingResult.geometry.location.lng)
    }

    boolean isValidGeoCodingResult(final GeocodingResult geocodingResult) {
        return Arrays.asList(geocodingResult.types).stream()
                .anyMatch { addressType ->
            ((AddressType) addressType) == AddressType.LOCALITY || ((AddressType) addressType) == AddressType.SUBLOCALITY
        } &&
                Arrays.asList(geocodingResult.addressComponents).stream()
                        .anyMatch { addressComponent -> ((String) ((AddressComponent) addressComponent)["longName"]).equalsIgnoreCase("Argentina") }
    }

    List<GeocodingResult> findGoogleLocations(final String location) {
        try {
            Arrays.asList(GeocodingApi.geocode(geoApiContext, location + " argentina")
                    .resultType().await())

        } catch (ApiException | InterruptedException | IOException e) {
            throw new IllegalArgumentException("Unable to process Locations", e)
        }
    }

    List<Location> findNearBy(String locationName) {
        Location location = repository.findByNameIgnoreCase(locationName)
        double latDown = location.getCoordinate().getLatitude() - 0.08
        double latTop = location.getCoordinate().getLatitude() + 0.08
        double lngDown = location.getCoordinate().getLongitude() - 0.08
        double lngTop = location.getCoordinate().getLongitude() + 0.08
        repository
                .findByCoordinateLatitudeBetweenAndCoordinateLongitudeBetween(latDown, latTop, lngDown,
                lngTop)
    }

    void delete(Location location) {
        repository.delete(location)
    }

    Location save(Location location) {
        Coordinate coordinateForLocation
        try {
            coordinateForLocation = findCoordinateForLocation(location.getName())

        } catch (Exception e) {
            coordinateForLocation = new Coordinate()
        }
        if (coordinateForLocation.getLatitude() != 0 & coordinateForLocation.getLongitude() != 0) {
            location.setCoordinate(coordinateForLocation)
        }
        repository.save(location)
    }

    Optional<Location> find(long locationId) {
        repository.findOne(locationId)
    }
}
