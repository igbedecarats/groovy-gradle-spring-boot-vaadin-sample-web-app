package app.services.domain

import org.springframework.data.repository.CrudRepository

interface ServiceCategoryRepository extends CrudRepository<ServiceCategory, Long> {

    Optional<ServiceCategory> findOne(final long id)

    ServiceCategory findByName(String serviceName)
}