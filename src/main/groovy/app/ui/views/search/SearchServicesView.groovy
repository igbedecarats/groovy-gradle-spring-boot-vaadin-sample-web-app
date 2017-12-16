package app.ui.views.search

import app.global.security.UserHolder
import app.locations.domain.LocationArea
import app.services.domain.Service
import app.services.domain.ServiceCategory
import app.services.usecase.ServiceInteractor
import app.ui.Sections
import app.ui.events.QuotationModifiedEvent
import app.ui.views.quotation.QuotationForm
import app.users.domain.User
import com.vaadin.data.HasValue
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.spring.annotation.SpringView
import com.vaadin.ui.*
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.spring.events.EventBus
import org.vaadin.spring.events.EventScope
import org.vaadin.spring.events.annotation.EventBusListenerMethod
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem

import java.time.DayOfWeek
import java.util.stream.Collectors

@SpringView(name = "")
@SideBarItem(sectionId = Sections.SEARCH, caption = "Search", order = 0)
@FontAwesomeIcon(FontAwesome.SEARCH)
class SearchServicesView extends CustomComponent implements View {

    private User loggedUser

    private final EventBus.SessionEventBus eventBus

    private final QuotationForm quotationForm

    private ServiceInteractor serviceInteractor

    private VerticalLayout servicesContainer = new VerticalLayout()

    private TextField searchTextField
    private CheckBox advanceSearch
    private CheckBox nearBy
    private CheckBoxGroup<String> areas
    private CheckBoxGroup<String> categories
    private NativeSelect<String> startDays
    private NativeSelect<String> endDays

    private List<String> existingAreas
    private List<String> existingCategories

    @Autowired
    SearchServicesView(EventBus.SessionEventBus eventBus, QuotationForm quotationForm,
                       ServiceInteractor serviceInteractor, UserHolder userHolder) {
        Validate.notNull(eventBus, "The Event Bus cannot be null")
        Validate.notNull(quotationForm, "The Quotation Form cannot be null")
        Validate.notNull(serviceInteractor, "The Service Interactor cannot be null")
        this.serviceInteractor = serviceInteractor
        this.eventBus = eventBus
        this.eventBus.subscribe(this)
        this.quotationForm = quotationForm
        loggedUser = userHolder.getUser()
        VerticalLayout searchLayout = new VerticalLayout()
        HorizontalLayout simpleSearchLayout = new HorizontalLayout()
        searchTextField = new TextField()
        searchTextField.setPlaceholder("Buscar por Nombre")
        searchTextField.addValueChangeListener(
                new HasValue.ValueChangeListener() {
                    @Override
                    void valueChange(final HasValue.ValueChangeEvent event) {
                        simpleSearch(searchTextField.getValue())
                    }
                }
        )
        searchTextField.setWidth("600px")
        searchTextField.setEnabled(true)
        nearBy = new CheckBox("Cerca tuyo")
        nearBy.setEnabled(true)
        nearBy.addValueChangeListener(
                new HasValue.ValueChangeListener() {
                    @Override
                    void valueChange(final HasValue.ValueChangeEvent event) {
                        simpleSearch(
                                StringUtils.isNotBlank(searchTextField.getValue()) ? searchTextField.getValue()
                                        : StringUtils.EMPTY)
                    }
                }
        )
        advanceSearch = new CheckBox("Búsqueda avanzada")
        simpleSearchLayout.addComponents(searchTextField, nearBy, advanceSearch)
        simpleSearchLayout.setSizeUndefined()
        simpleSearchLayout.setComponentAlignment(searchTextField, Alignment.TOP_CENTER)
        simpleSearchLayout.setComponentAlignment(advanceSearch, Alignment.TOP_CENTER)
        HorizontalLayout advancedSearchLayout = new HorizontalLayout()
        advancedSearchLayout.setVisible(false)
        advanceSearch.addValueChangeListener(
                new HasValue.ValueChangeListener() {
                    @Override
                    void valueChange(final HasValue.ValueChangeEvent event) {
                        advancedSearchLayout.setVisible(!advancedSearchLayout.isVisible())
                        searchTextField.setEnabled(!searchTextField.isEnabled())
                        nearBy.setEnabled(!nearBy.isEnabled())

                    }
                })
        existingAreas = Arrays.asList(LocationArea.values()).stream()
                .map { locationArea -> (locationArea as LocationArea).getValue() }
                .collect(Collectors.toList())
        areas = new CheckBoxGroup<>("Areas", existingAreas)
        existingCategories = serviceInteractor.findAllCategories().stream()
                .map { serviceCategory -> ((ServiceCategory) serviceCategory).getName() }.collect(
                Collectors.toList())
        categories = new CheckBoxGroup<>("Categorias", existingCategories)
        startDays = new NativeSelect<>("Dia Desde", Service.getLocalizedDaysOfTheWeek())
        endDays = new NativeSelect<>("Dia Hasta", Service.getLocalizedDaysOfTheWeek())
        Button advanceSearchButton = new Button()
        advanceSearchButton.setIcon(VaadinIcons.SEARCH)
        advanceSearchButton.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(final Button.ClickEvent event) {
                advanceSearch()
            }
        })
        VerticalLayout advanceSearchButtonVerticalLayout = new VerticalLayout(advanceSearchButton)
        advanceSearchButtonVerticalLayout.setComponentAlignment(advanceSearchButton, Alignment.MIDDLE_CENTER)
        advancedSearchLayout
                .addComponents(areas, categories, startDays, endDays, advanceSearchButtonVerticalLayout)
        searchLayout.addComponents(simpleSearchLayout, advancedSearchLayout)

        VerticalLayout rootLayout = new VerticalLayout()
        rootLayout.addComponent(searchLayout)
        rootLayout.setSizeFull()
        rootLayout.setComponentAlignment(searchLayout, Alignment.TOP_CENTER)

        Panel panel = new Panel("Servicios")
        panel.setWidth("1000px")
        panel.setHeight("380px")
        servicesContainer.setSizeUndefined()
        panel.setContent(servicesContainer)
        rootLayout.addComponent(panel)
        setCompositionRoot(rootLayout)
        simpleSearch("")
    }

    private void advanceSearch() {
        if (advanceSearch.getValue()) {
            servicesContainer.removeAllComponents()
            List<LocationArea> searchAreas
            if (this.areas.getSelectedItems().isEmpty()) {
                searchAreas = Arrays.asList(LocationArea.values())
            } else {
                searchAreas = new ArrayList<>(this.areas.getSelectedItems().stream()
                        .map { area -> LocationArea.getByValue(area as String) }.collect(
                        Collectors.toList()))
            }
            List<String> searchCategories
            if (this.categories.getSelectedItems().isEmpty()) {
                searchCategories = existingCategories
            } else {
                searchCategories = new ArrayList<>(this.categories.getSelectedItems())
            }
            Integer searchStartDay
            if (startDays.isEmpty()) {
                searchStartDay = DayOfWeek.of(1).getValue()
            } else {
                searchStartDay = Service.getDayOfTheWeekFromLocalizedDay(startDays.getSelectedItem().get())
            }
            Integer searchEndDays
            if (endDays.isEmpty()) {
                searchEndDays = DayOfWeek.of(7).getValue()
            } else {
                searchEndDays = Service.getDayOfTheWeekFromLocalizedDay(endDays.getSelectedItem().get())
            }
            List<Service> services = serviceInteractor
                    .searchBy(StringUtils.EMPTY, searchAreas, searchCategories, searchStartDay,
                    searchEndDays)
            populateServicesContainer(services)
        }
    }

    private void simpleSearch(String value) {
        List<Service> services = removeLoggedUserFromServiceList(
                serviceInteractor.findAllMatchingName(value, loggedUser, nearBy.getValue()))
        populateServicesContainer(services)
    }

    private List<Service> removeLoggedUserFromServiceList(List<Service> services) {
        services.stream()
                .filter { service -> ((service as Service).getProvider() != loggedUser) }.collect(Collectors.toList())
    }

    private void populateServicesContainer(List<Service> services) {
        servicesContainer.removeAllComponents()
        services.stream()
                .forEach { service ->
            servicesContainer
                    .addComponent(new SearchServiceComponent(service as Service, loggedUser, serviceInteractor,
                    quotationForm))
        }
    }

    @EventBusListenerMethod(scope = EventScope.SESSION)
    void onQuotationModifiedEvent(QuotationModifiedEvent event) {
        simpleSearch("")
        quotationForm.closePopup()
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
