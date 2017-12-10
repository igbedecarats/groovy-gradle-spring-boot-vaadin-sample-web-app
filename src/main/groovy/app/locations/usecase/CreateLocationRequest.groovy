package app.locations.usecase

import app.locations.domain.LocationArea
import org.apache.commons.lang3.StringUtils

class CreateLocationRequest {

    private String name
    private LocationArea area

    CreateLocationRequest(final String name, final String area) {
        this.name = name
        this.area = StringUtils.isNotBlank(area) ? LocationArea.valueOf(area) : null
    }

    String getName() {
        name
    }

    LocationArea getArea() {
        area
    }
}
