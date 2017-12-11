package app.ui.views.service

import app.services.domain.Service
import app.services.usecase.ServiceInteractor
import app.users.domain.User
import com.vaadin.shared.ui.ContentMode
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.Validate

abstract class AbstractServiceComponent extends VerticalLayout {

    protected Label name = new Label()
    protected Label description = new Label()
    protected Label category = new Label()
    protected Label subCategory = new Label()
    protected Label locationName = new Label()
    protected Label locationArea = new Label()
    protected Label times = new Label()
    protected Label days = new Label()
    protected Label provider = new Label()
    protected Label rating = new Label()
    protected Service service

    protected HorizontalLayout providerContainer

    AbstractServiceComponent(final Service service, final User loggedUser,
                             final ServiceInteractor serviceInteractor) {
        Validate.notNull(service, "The Service cannot be null.")
        Validate.notNull(loggedUser, "The User cannot be null.")
        Validate.notNull(serviceInteractor, "The ServiceInteractor cannot be null.")
        this.service = service

        name.setCaption("<h4> Servicio </h4> ")
        name.setCaptionAsHtml(true)
        name.setValue(service.getName())
        name.setContentMode(ContentMode.TEXT)
        name.setStyleName(ValoTheme.LABEL_BOLD)

        description.setCaption("<h4> Descripción </h4>")
        description.setCaptionAsHtml(true)
        description.setValue(service.getDescription())

        HorizontalLayout hl1 = new HorizontalLayout(name, description)

        HorizontalLayout categoriesAndLocationContainer = new HorizontalLayout()
        HorizontalLayout categoriesContainer = new HorizontalLayout()
        categoriesContainer.setCaption("<h4> Categoría </h4>")
        categoriesContainer.setCaptionAsHtml(true)
        category.setValue(service.getCategory().getName())
        String subCategoryName = service.getSubCategory().getName()
        subCategory
                .setValue(StringUtils.isNotBlank(subCategoryName) ? subCategoryName : StringUtils.EMPTY)
        categoriesContainer.addComponents(category, subCategory)
        HorizontalLayout locationsContainer = new HorizontalLayout()
        locationsContainer.setCaption("<h4> Ubicación </h4>")
        locationsContainer.setCaptionAsHtml(true)
        locationArea.setValue(service.getLocation().getArea().getValue())
        locationName.setValue(service.getLocation().getName())
        locationsContainer.addComponents(locationArea, locationName)
        categoriesAndLocationContainer.addComponents(categoriesContainer, locationsContainer)


        HorizontalLayout dayTimeContainer = new HorizontalLayout()
        dayTimeContainer.setCaption("<h4> Días y Horarios de Atención </h4>")
        dayTimeContainer.setCaptionAsHtml(true)
        days.setValue(service.getLocalizedStartDay() + " a " + service.getLocalizedEndDay())
        times.setValue(service.getStartTime() + " a " + service.getEndTime())
        dayTimeContainer.addComponents(days, times)
        addComponent(dayTimeContainer)

        HorizontalLayout hl2 = new HorizontalLayout(categoriesAndLocationContainer, dayTimeContainer)

        providerContainer = new HorizontalLayout()
        provider
                .setValue(service.getProvider().getFirstName() + " " + service.getProvider().getLastName())
        provider.setVisible(loggedUser != service.getProvider())
        provider.setStyleName(ValoTheme.LABEL_H4)
        rating.setValue(Float.toString(serviceInteractor.calculateRate(service).getRating()))
        providerContainer.addComponents(provider, rating)
        rating.setStyleName(ValoTheme.LABEL_H4)

        addComponents(hl1, hl2, providerContainer)

        setSizeUndefined()
    }

    protected void addButtonsToProviderContainer(Component... components) {
        providerContainer.addComponents(components)
    }
}
