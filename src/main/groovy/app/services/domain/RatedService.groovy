package app.services.domain

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes = 'service,rating')
class RatedService extends Service {

    protected Service service

    protected Float rating

    RatedService(Service service, float rating) {
        super(service.getName(), service.getDescription(), service.getProvider(), service.getLocation(),
                service.getCategory(), service.getSubCategory(), service.getStartTime(),
                service.getEndTime(), service.getStartDay(), service.getEndDay())
        this.service = service
        this.rating = rating
    }

    Service getService() {
        service
    }

    Float getRating() {
        rating
    }
}
