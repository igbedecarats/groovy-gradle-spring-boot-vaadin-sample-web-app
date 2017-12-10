package app.services.usecase

class CreateServiceRequest {

    private long providerId
    private String name
    private String description
    private long locationId
    private long categoryId
    private long subCategoryId
    private String startTime
    private String endTime
    private Integer startDay
    private Integer endDay

    CreateServiceRequest(
            final long providerId,
            final String name,
            final String description,
            final long locationId,
            final long categoryId,
            final long subCategoryId,
            final String startTime, final String endTime, final Integer startDay, final Integer endDay) {
        this.providerId = providerId
        this.name = name
        this.description = description
        this.locationId = locationId
        this.categoryId = categoryId
        this.subCategoryId = subCategoryId
        this.startTime = startTime
        this.endTime = endTime
        this.startDay = startDay
        this.endDay = endDay
    }

    long getProviderId() {
        providerId
    }

    String getName() {
        name
    }

    String getDescription() {
        description
    }

    long getLocationId() {
        locationId
    }

    long getCategoryId() {
        categoryId
    }

    long getSubCategoryId() {
        subCategoryId
    }

    String getStartTime() {
        startTime
    }

    String getEndTime() {
        endTime
    }

    Integer getStartDay() {
        startDay
    }

    Integer getEndDay() {
        endDay
    }
}