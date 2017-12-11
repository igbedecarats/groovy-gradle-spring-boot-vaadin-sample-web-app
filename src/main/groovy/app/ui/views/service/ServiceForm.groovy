package app.ui.views.service

import app.global.security.UserHolder
import app.locations.domain.Location
import app.locations.usecase.LocationInteractor
import app.services.domain.Service
import app.services.domain.ServiceCategory
import app.services.domain.ServiceSubCategory
import app.services.usecase.ServiceInteractor
import app.ui.events.ServiceCreatedEvent
import app.users.domain.User
import com.vaadin.data.Binder
import com.vaadin.data.HasValue
import com.vaadin.spring.annotation.SpringComponent
import com.vaadin.spring.annotation.UIScope
import com.vaadin.ui.*
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.spring.events.EventBus
import org.vaadin.viritin.form.AbstractForm
import org.vaadin.viritin.form.AbstractForm.SavedHandler
import org.vaadin.viritin.form.AbstractForm.ResetHandler
import org.vaadin.viritin.layouts.MFormLayout
import org.vaadin.viritin.layouts.MVerticalLayout

import java.util.stream.Collectors

@UIScope
@SpringComponent
class ServiceForm extends AbstractForm<Service> {

    private User loggedUser
    private Service service
    private ServiceInteractor serviceInteractor
    private LocationInteractor locationInteractor
    private UserHolder userHolder
    private final EventBus.SessionEventBus eventBus

    private Binder<Service> binder = new Binder<>(Service.class)

    private TextField name = new TextField("Nombre")
    private TextField description = new TextField("Descripción")
    private List<String> existingCategories
    private NativeSelect<String> categories = new NativeSelect<>("Categorias")
    private List<String> existingSubCategories
    private NativeSelect<String> subCategories = new NativeSelect<>("Sub Categorias")
    private List<String> existingLocations
    private NativeSelect<String> locations = new NativeSelect<>("Ubicación")
    private TextField startTime = new TextField("Tiempo Inicio")
    private TextField endTime = new TextField("Tiempo Fin")
    private List<String> daysOfTheWeek = Service.getLocalizedDaysOfTheWeek()
    private NativeSelect<String> startDays = new NativeSelect<>("Dia Inicio")
    private NativeSelect<String> endDays = new NativeSelect<>("Dia Fin")

    private final HorizontalLayout categoriesContainer = new HorizontalLayout()
    private final HorizontalLayout timesContainer = new HorizontalLayout()
    private final HorizontalLayout daysContainer = new HorizontalLayout()

    @Autowired
    ServiceForm(ServiceInteractor serviceInteractor,
                LocationInteractor locationInteractor,
                EventBus.SessionEventBus eventBus,
                UserHolder userHolder) {
        super(Service.class)
        this.userHolder = userHolder
        this.loggedUser = this.userHolder.getUser()
        this.service = new Service()
        this.serviceInteractor = serviceInteractor
        this.locationInteractor = locationInteractor
        this.eventBus = eventBus

        binder.bindInstanceFields(this)

        updateCategories()
        categories.setItems(existingCategories)
        categories.setEmptySelectionAllowed(false)
        categories.setSelectedItem(existingCategories.stream().findFirst().get())
        categories.addValueChangeListener(
                new HasValue.ValueChangeListener() {

                    @Override
                    void valueChange(final HasValue.ValueChangeEvent event) {
                        updateSubCategories()
                        subCategories.setItems(existingSubCategories)
                        subCategories.setSelectedItem(existingSubCategories.stream().findFirst().get())
                    }
                })


        updateSubCategories()
        subCategories.setItems(existingSubCategories)
        subCategories.setSelectedItem(existingSubCategories.stream().findFirst().get())

        categoriesContainer.addComponents(categories, subCategories)

        updateLocations()
        locations.setItems(existingLocations)
        locations.setEmptySelectionAllowed(false)

        timesContainer.addComponents(startTime, endTime)

        startDays.setItems(daysOfTheWeek)
        endDays.setItems(daysOfTheWeek)
        startDays.setSelectedItem(daysOfTheWeek.stream().findFirst().get())
        endDays.setSelectedItem(daysOfTheWeek.stream().findFirst().get())
        daysContainer.addComponents(startDays, endDays)

        setSavedHandler(new SavedHandler() {

            @Override
            void onSave(final Object entity) {
                save()
            }
        })
        setResetHandler(new ResetHandler() {
            @Override
            void onReset(final Object entity) {
                eventBus.publish(this, new ServiceCreatedEvent(service))
            }
        })
        setModalWindowTitle("Servicio")
        this.setSizeUndefined()
    }

    private void updateCategories() {
        existingCategories = this.serviceInteractor.findAllCategories().stream()
                .map { category -> ((ServiceCategory) category).getName() }.collect(
                Collectors.toList())
    }

    private void updateSubCategories() {
        if (this.categories.getSelectedItem().isPresent()) {
            ServiceCategory category = serviceInteractor
                    .findCategoryByName(this.categories.getSelectedItem().get())
            this.existingSubCategories = category.getSubCategories().stream()
                    .map { serviceSubCategory -> ((ServiceSubCategory) serviceSubCategory).getName() }.collect(
                    Collectors.toList())
        }
    }

    private void updateLocations() {
        existingLocations = this.locationInteractor.findAll().stream()
                .map { location -> ((Location) location).getName() }.collect(
                Collectors.toList())
    }

    private void save() {
        try {
            service.setProvider(loggedUser)
            String categoryName = categories.getSelectedItem()
                    .orElseThrow { -> new IllegalArgumentException("The Service Category is mandatory") }
            service.setCategory(serviceInteractor.findCategoryByName(categoryName))
            if (subCategories.getSelectedItem().isPresent()) {
                service.setSubCategory(service.getCategory().getSubCategories().stream().filter {
                    serviceSubCategory -> (((ServiceSubCategory) serviceSubCategory).getName() == subCategories.getSelectedItem().get())
                }
                .findFirst().get())
            }
            String locationName = locations.getSelectedItem()
                    .orElseThrow { -> new IllegalArgumentException("The Location is mandatory") }
            service.setLocation(locationInteractor.findByName(locationName))
            if (startDays.getSelectedItem().isPresent()) {
                service.setLocalizedStartDay(startDays.getSelectedItem().get())
            }
            if (endDays.getSelectedItem().isPresent()) {
                service.setLocalizedEndDay(endDays.getSelectedItem().get())
            }
            serviceInteractor.save(service)
            eventBus.publish(this, new ServiceCreatedEvent(service))
            Notification
                    .show("Éxito!", Notification.Type.HUMANIZED_MESSAGE)
        } catch (Exception ignored) {
            Notification
                    .show("Unable to process request, please contact the system admin", Notification.Type.ERROR_MESSAGE)
        }
    }

    void setService(Service service) {
        this.service = service
        binder.setBean(service)
        updateLocations()
        locations.setItems(existingLocations)
        locations.setSelectedItem(
                loggedUser.getLocation() != null ? loggedUser.getLocation().getName()
                        : existingLocations.stream().findFirst().get())
        updateCategories()
        categories.setItems(existingCategories)
        if (service.getCategory() == null) {
            categories.setSelectedItem(existingCategories.stream().findFirst().get())
            updateSubCategories()
            subCategories.setSelectedItem(existingSubCategories.stream().findFirst().get())
        } else {
            categories.setSelectedItem(service.getCategory().getName())
            updateSubCategories()
            if (service.getSubCategory() != null) {
                subCategories.setSelectedItem(service.getSubCategory().getName())
            }
        }
        if (service.getStartTime() != null) {
            startTime.setValue(service.getStartTime())
        } else {
            startTime.setValue("00:00")
        }
        if (service.getEndTime() != null) {
            endTime.setValue(service.getEndTime())
        } else {
            endTime.setValue("00:00")
        }

        startDays.setItems(daysOfTheWeek)
        endDays.setItems(daysOfTheWeek)
        startDays.setSelectedItem(service.getStartDay() != null ? service.getLocalizedStartDay()
                : daysOfTheWeek.stream().findFirst().get())
        endDays.setSelectedItem(service.getEndDay() != null ? service.getLocalizedEndDay()
                : daysOfTheWeek.stream().findFirst().get())
    }

    @Override
    protected Component createContent() {
        new MVerticalLayout(
                new MFormLayout(name, description, categoriesContainer, locations, timesContainer,
                        daysContainer).withWidth(""),
                getToolbar()).withWidth("")
    }
}
