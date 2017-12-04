package app.locations.domain

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Coordinate {

    @Column(name = "latitude", nullable = false)
    private double latitude

    @Column(name = "longitude", nullable = false)
    private double longitude

    double getLatitude() {
        return latitude
    }

    double getLongitude() {
        return longitude
    }
}
