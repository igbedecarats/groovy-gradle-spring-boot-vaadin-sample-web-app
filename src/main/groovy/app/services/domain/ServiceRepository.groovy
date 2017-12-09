package app.services.domain

import app.locations.domain.Location
import app.locations.domain.LocationArea

interface ServiceRepository {

    List<Service> findByProviderIdAndNameIgnoreCaseContaining(final long id, final String name)

    List<Service> findByProviderId(long id)

    List<Service> findByNameIgnoreCaseContaining(String value)

    List<Service> findByNameIgnoreCaseContainingAndLocationAreaInAndCategoryNameInAndStartDayGreaterThanEqualAndEndDayLessThanEqual(
            String likeFilter, List<LocationArea> searchAreas, List<String> searchCategories,
            Integer searchStartDay, Integer searchEndDays)

    List<Service> findByNameIgnoreCaseContainingAndLocationIn(String likeFilter,
                                                              List<Location> locations)
}