package app.locations.domain

import org.springframework.data.jpa.repository.JpaRepository

interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findOne(final long id)

    Location findByName(String locationName)

    List<Location> findByNameIgnoreCaseContaining(final String name)

    Location findByNameIgnoreCase(final String name)

    List<Location> findByCoordinateLatitudeBetweenAndCoordinateLongitudeBetween(final double latDown,
                                                                                final double latUp,
                                                                                final double lngDown,
                                                                                final double lngUp)
}
