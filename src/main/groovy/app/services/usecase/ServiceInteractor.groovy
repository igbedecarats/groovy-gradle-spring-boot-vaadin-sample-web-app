package app.services.usecase

import app.feedbacks.domain.Feedback
import app.feedbacks.domain.FeedbackRepository
import app.locations.domain.Location
import app.locations.domain.LocationArea
import app.locations.usecase.LocationInteractor
import app.services.domain.*
import app.users.domain.User
import app.users.domain.UserRepository
import com.google.gwt.thirdparty.guava.common.collect.Sets
import org.apache.commons.lang3.Validate

import javax.persistence.EntityNotFoundException
import java.util.stream.Collectors

class ServiceInteractor {

    private ServiceRepository serviceRepository

    private ServiceCategoryRepository serviceCategoryRepository

    private ServiceSubCategoryRepository serviceSubCategoryRepository

    private UserRepository userRepository

    private LocationInteractor locationInteractor

    private FeedbackRepository feedbackRepository

    ServiceInteractor(ServiceRepository serviceRepository,
                      ServiceCategoryRepository serviceCategoryRepository,
                      ServiceSubCategoryRepository serviceSubCategoryRepository,
                      UserRepository userRepository,
                      LocationInteractor locationInteractor,
                      FeedbackRepository feedbackRepository) {
        this.serviceRepository = serviceRepository
        this.serviceCategoryRepository = serviceCategoryRepository
        this.serviceSubCategoryRepository = serviceSubCategoryRepository
        this.userRepository = userRepository
        this.locationInteractor = locationInteractor
        this.feedbackRepository = feedbackRepository
    }

    Service find(final long serviceId) {
        Service service = serviceRepository.findOne(serviceId)
        if (service == null) {
            throw new EntityNotFoundException("The Service with id " + serviceId + " doesn't exist.")
        }
        service
    }

    Service create(CreateServiceRequest request) {
        Validate.notNull(request, "The CreateServiceRequest cannot be null.")
        User provider = userRepository.findOne(request.getProviderId())
        Location location = locationInteractor.find(request.getLocationId()).orElseThrow {
            -> new EntityNotFoundException("The location with id " + request.getLocationId() + " doesn't exist.")
        };
        ServiceCategory category = this.getServiceCategory(request.getCategoryId())
        ServiceSubCategory subCategory = null
        if (request.getSubCategoryId() != 0) {
            subCategory = serviceSubCategoryRepository.findOne(request.getSubCategoryId())
                    .orElseThrow { ->
                new EntityNotFoundException(
                        "The sub category with id " + request.getSubCategoryId() + " doesn't exist.")
            }
        }
        Service service = new Service(request.getName(), request.getDescription(), provider, location,
                category, subCategory, request.getStartTime(), request.getEndTime(),
                request.getStartDay(), request.getEndDay())

        serviceRepository.save(service)
    }

    Set<ServiceCategory> getServiceCategories() {
        Sets.newHashSet(serviceCategoryRepository.findAll())
    }

    ServiceCategory getServiceCategory(long categoryId) {
        serviceCategoryRepository.findOne(categoryId).orElseThrow {
            ->
            new EntityNotFoundException(
                    "The category with id " + categoryId + " doesn't exist.")
        }
    }

    void delete(Service service) {
        serviceRepository.delete(service)
    }

    List<ServiceCategory> findAllCategories() {
        (List<ServiceCategory>) serviceCategoryRepository.findAll()
    }

    ServiceCategory findCategoryByName(String categoryName) {
        serviceCategoryRepository.findByName(categoryName)
    }

    Service save(Service service) {
        serviceRepository.save(service)
    }

    List<Service> findAllByProviderId(long id) {
        List<Service> services = serviceRepository.findByProviderId(id)
        services
    }

    List<Service> findAllByProviderMatchingName(User provider, String name) {
        String likeFilter = "%" + name.replace(" ", "%") + "%"
        List<Service> services = serviceRepository
                .findByProviderIdAndNameIgnoreCaseContaining(provider.getId(), "%" + likeFilter + "%")
        services
    }

    List<Service> findAllMatchingName(String value,
                                      User loggedUser, Boolean searchNearby) {
        String likeFilter = "%" + value.replace(" ", "%") + "%"
        List<Service> services
        if (searchNearby) {
            List<Location> locations = locationInteractor.findNearBy(loggedUser.getLocation().getName())
            services = serviceRepository
                    .findByNameIgnoreCaseContainingAndLocationIn("%" + likeFilter + "%", locations)
        } else {
            services = serviceRepository
                    .findByNameIgnoreCaseContaining("%" + likeFilter + "%")
        }
        orderServicesByFeedbackRatingDesc(services)
    }

    private List<Service> orderServicesByFeedbackRatingDesc(List<Service> services) {
        calculateRates(services).stream().map { ratedService -> ((RatedService) ratedService)["service"] }.collect(
                Collectors.toList())
    }

    List<RatedService> calculateRates(List<Service> services) {
        List<RatedService> ratedServices = services.stream()
                .map { service -> this.calculateRate((Service)service) }
                .collect(Collectors.toList())
        ratedServices.sort(Comparator.comparing {
            ((RatedService) it).rating
        }.reversed() as Comparator<? super RatedService>)
        ratedServices
    }

    RatedService calculateRate(Service service) {
        User user = service.getProvider()
        List<Feedback> feedbacks = feedbackRepository
                .findByContractServiceIdAndRecipientId(service.getId(), user.getId())
        float rating = 0f
        for (Feedback feedback : feedbacks) {
            rating += feedback.getRating()
        }
        if (feedbacks.size() > 0) {
            rating = (float) ((float) rating / (float) feedbacks.size())
        }
        new RatedService(service, rating)
    }

    List<Service> searchBy(String searchName, List<LocationArea> searchAreas,
                           List<String> searchCategories,
                           Integer searchStartDay, Integer searchEndDays) {
        String likeFilter = "%" + searchName.replace(" ", "%") + "%"
        likeFilter = "%" + likeFilter + "%"
        List<Service> services = serviceRepository
                .findByNameIgnoreCaseContainingAndLocationAreaInAndCategoryNameInAndStartDayGreaterThanEqualAndEndDayLessThanEqual(
                likeFilter, searchAreas, searchCategories, searchStartDay, searchEndDays)
        orderServicesByFeedbackRatingDesc(services)
    }
}