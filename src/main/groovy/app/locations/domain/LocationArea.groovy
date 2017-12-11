package app.locations.domain

enum LocationArea {

    CABA("Capital Federal"), GBA_SUR("GBA Sur"), GBA_NORTE("GBA Norte"), GBA_OESTE("GBA Oeste")

    private final String value

    LocationArea(String value) {
        this.value = value
    }

    String getValue() {
        value
    }

    static LocationArea getByValue(final String theValue) {
        List<LocationArea> locationAreas = Arrays.asList(LocationArea.values())
        return locationAreas.stream()
                .filter({ locationArea -> ((LocationArea) locationArea)["value"] == theValue }).findFirst()
                .orElseThrow { -> new IllegalArgumentException("No Area was found with " + theValue) }
    }
}
