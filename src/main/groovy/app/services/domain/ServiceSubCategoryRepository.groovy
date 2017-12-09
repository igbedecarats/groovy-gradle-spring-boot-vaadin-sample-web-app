package app.services.domain

import org.springframework.data.repository.CrudRepository

interface ServiceSubCategoryRepository extends CrudRepository<ServiceSubCategory, Long> {

    List<ServiceSubCategory> findByIdIn(final List<Long> ids)

    Optional<ServiceSubCategory> findOne(final long id)

}
