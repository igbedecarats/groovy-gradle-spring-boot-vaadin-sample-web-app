package app.services.domain

import app.locations.domain.Location
import app.users.domain.User
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.Validate
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder

import javax.persistence.*
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.util.stream.Collectors

@Entity
@Table(name = "service")
@JsonIgnoreProperties(ignoreUnknown = true)
class Service {

    @Id
    @GeneratedValue
    protected long id

    @Column(name = "name", nullable = false)
    protected String name

    @Column(name = "description", nullable = false)
    protected String description

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    protected ServiceCategory category

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    protected ServiceSubCategory subCategory

    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_id")
    protected User provider

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id")
    protected Location location

    @Column(name = "start_time")
    protected String startTime

    @Column(name = "end_time")
    protected String endTime

    @Column(name = "start_day")
    protected Integer startDay

    @Column(name = "end_day")
    protected Integer endDay

    Service() {}

    Service(String name, String description,
            ServiceCategory category, User provider, Location location) {
        this(name, description, provider, location, category, null, null, null, null, null)
    }

    Service(String name, String description,
            User provider, Location location, ServiceCategory category,
            ServiceSubCategory subCategory, String startTime, String endTime,
            Integer startDay, Integer endDay) {
        Validate.notBlank(name, "The Service name cannot be blank.")
        Validate.notBlank(description, "The Service description cannot be blank.")
        Validate.notNull(provider, "The Service provider cannot be blank.")
        Validate.notNull(location, "The Service location cannot be blank.")
        Validate.notNull(category, "The Service category cannot be blank.")
        this.name = name
        this.description = description
        this.category = category
        this.provider = provider
        this.location = location
        this.subCategory = subCategory
        setTimesAndDays(startTime, endTime, startDay, endDay)
    }

    long getId() {
        id
    }

    String getName() {
        name
    }

    String getDescription() {
        description
    }

    ServiceCategory getCategory() {
        category
    }

    ServiceSubCategory getSubCategory() {
        subCategory
    }

    User getProvider() {
        provider
    }

    Location getLocation() {
        location
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

    void setTimesAndDays(String startTime, String endTime, Integer startDay,
                         Integer endDay) {
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime) && startDay != null
                && endDay != null) {
            validateDayTimesRange(startTime, endTime)
            validateDaysRange(startDay, endDay)
            this.startTime = startTime
            this.endTime = endTime
            this.startDay = startDay
            this.endDay = endDay
        } else {
            this.startTime = null
            this.endTime = null
            this.startDay = null
            this.endDay = null
        }
    }

    LocalTime getLocalStartTime() {
        LocalTime.parse(startTime)
    }

    LocalTime getLocalEndTime() {
        LocalTime.parse(endTime)
    }

    String getLocalizedStartDay() {
        startDay != null ? getLocalizedDayOfTheWeek(startDay) : null
    }

    String getLocalizedEndDay() {
        endDay != null ? getLocalizedDayOfTheWeek(endDay) : null
    }

    void setLocalizedStartDay(final String day) {
        this.startDay = getDayOfTheWeekFromLocalizedDay(day)
    }

    void setLocalizedEndDay(final String day) {
        this.endDay = getDayOfTheWeekFromLocalizedDay(day)
    }

    static int getDayOfTheWeekFromLocalizedDay(String day) {
        Arrays.stream(DayOfWeek.values()).filter { dayOfWeek ->
            StringUtils.capitalize(DayOfWeek.of((int) (((Enum) dayOfWeek)["value"]))
                    .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"))) == day
        }.findFirst().orElseThrow { -> new IllegalArgumentException("Invalid Day of the Week") }.getValue()
    }

    private String getLocalizedDayOfTheWeek(final Integer dayOfTheWeek) {
        StringUtils.capitalize(
                DayOfWeek.of(dayOfTheWeek).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")))
    }

    static List<String> getLocalizedDaysOfTheWeek() {
        Arrays.stream(DayOfWeek.values()).map { dayOfWeek ->
            StringUtils.capitalize(DayOfWeek.of((int) (((Enum) dayOfWeek)["value"]))
                    .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")))
        }.collect(Collectors.toList())
    }

    private void validateDaysRange(final Integer startDay, final Integer endDay) {
        try {
            DayOfWeek.of(startDay)
            DayOfWeek.of(endDay)
            Validate.isTrue(startDay <= endDay, "The Start Day must be less or equal to the End Day")
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Invalid Day of the Week", e)
        }
    }

    private void validateDayTimesRange(final String startTime, final String endTime) {
        try {
            LocalTime localStartTime = LocalTime.parse(startTime)
            LocalTime localEndTime = LocalTime.parse(endTime)
            if (!localStartTime.equals(localEndTime)) {
                Validate.isTrue(localStartTime.isBefore(localEndTime),
                        "The Start Time must be earlier than the End Time.")
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid Time of the Day: " + e.getParsedString(), e)
        }
    }

    @Override
    boolean equals(final Object o) {
        if (this == o) {
            true
        }

        if (o == null || getClass() != o.getClass()) {
            false
        }

        Service service = (Service) o

        new EqualsBuilder().append(id, service.id).append(provider, service.provider)
                .append(name, service.name).append(description, service.description)
                .append(category, service.category).isEquals()
    }

    @Override
    int hashCode() {
        new HashCodeBuilder(17, 37).append(id).append(provider).append(name).append(description)
                .append(category).toHashCode()
    }

    @Override
    String toString() {
        new ToStringBuilder(this).append("id", id).append("provider", provider)
                .append("name", name).append("description", description).append("category", category)
                .toString()
    }
}
