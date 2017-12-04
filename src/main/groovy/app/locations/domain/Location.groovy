package app.locations.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.persistence.*

@Entity
@Table(name = "location")
@JsonIgnoreProperties(ignoreUnknown = true)
class Location {

    @Id
    @GeneratedValue
    long id

    @Column(name = "name", nullable = false, unique = true)
    String name

    @Column(name = "area")
    @Enumerated(EnumType.STRING)
    LocationArea area

    Coordinate coordinate = new Coordinate()

    double getLatitude() {
        return coordinate.getLatitude()
    }

    double getLongitude() {
        return coordinate.getLongitude()
    }

    Location(String name, LocationArea area) {
        this(name, area, new Coordinate())
    }

    Location() {}

    Location(final String name, final LocationArea area, final Coordinate coordinate) {
        this.name = name
        this.area = area
        this.coordinate = coordinate
    }

    long getId() {
        return id
    }

    String getName() {
        return name
    }

    LocationArea getArea() {
        return area
    }

    Coordinate getCoordinate() {
        return coordinate
    }

    void setName(final String name) {
        this.name = name
    }

    void setArea(final LocationArea area) {
        this.area = area
    }

    void setCoordinate(final Coordinate coordinate) {
        this.coordinate = coordinate
    }

    boolean equals(final o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Location location = (Location) o

        if (id != location.id) return false
        if (area != location.area) return false
        if (coordinate != location.coordinate) return false
        if (name != location.name) return false

        return true
    }

    int hashCode() {
        int result
        result = (int) (id ^ (id >>> 32))
        result = 31 * result + name.hashCode()
        result = 31 * result + area.hashCode()
        result = 31 * result + coordinate.hashCode()
        return result
    }
}
