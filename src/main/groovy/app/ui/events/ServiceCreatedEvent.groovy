package app.ui.events

import app.services.domain.Service

class ServiceCreatedEvent implements Serializable {

    private Service service

    ServiceCreatedEvent(final Service service) {
        this.service = service
    }

    Service getService() {
        service
    }
}
