package app.ui.views.service

import app.global.security.UserHolder
import app.services.domain.Service
import app.services.usecase.ServiceInteractor
import app.ui.Sections
import app.ui.events.ServiceCreatedEvent
import app.users.domain.User
import com.vaadin.data.HasValue
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.spring.annotation.SpringView
import com.vaadin.ui.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.vaadin.spring.events.EventBus
import org.vaadin.spring.events.EventScope
import org.vaadin.spring.events.annotation.EventBusListenerMethod
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem

//@Secured("ROLE_PROVIDER")
@SpringView(name = "myservices")
@SideBarItem(sectionId = Sections.SERVICES, caption = "Mis Servicios", order = 0)
@FontAwesomeIcon(FontAwesome.CUBES)
class MyServicesView extends CustomComponent implements View {

    private User loggedUser

    private ServiceInteractor serviceInteractor

    private final EventBus.SessionEventBus eventBus

    private ServiceForm form

    private VerticalLayout servicesContainer = new VerticalLayout()


    @Autowired
    MyServicesView(ServiceForm form, EventBus.SessionEventBus eventBus,
                   ServiceInteractor serviceInteractor, UserHolder userHolder) {

        this.serviceInteractor = serviceInteractor
        this.eventBus = eventBus
        this.eventBus.subscribe(this)
        this.form = form
        loggedUser = userHolder.getUser()

        VerticalLayout searchAndAddLayout = new VerticalLayout()
        HorizontalLayout hl = new HorizontalLayout()
        hl.setSizeUndefined()
        TextField searchTextField = new TextField()
        searchTextField.setPlaceholder("Buscar por nombre")
        searchTextField.addValueChangeListener(new HasValue.ValueChangeListener() {
            @Override
            void valueChange(final HasValue.ValueChangeEvent event) {
                search(searchTextField.getValue())
            }
        })
        searchTextField.setWidth("600px")
        Button addButton = new Button()
        addButton.setIcon(VaadinIcons.PLUS)
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(final Button.ClickEvent event) {
                add()
            }
        })
        hl.addComponentsAndExpand(searchTextField, addButton)
        searchAndAddLayout.addComponents(hl)
        searchAndAddLayout.setSizeUndefined()
        searchAndAddLayout.setComponentAlignment(hl, Alignment.TOP_CENTER)
        VerticalLayout rootLayout = new VerticalLayout()
        rootLayout.addComponent(searchAndAddLayout)
        rootLayout.setSizeFull()
        rootLayout.setComponentAlignment(searchAndAddLayout, Alignment.TOP_CENTER)

        Panel panel = new Panel("Servicios")
        panel.setWidth("1000px")
        panel.setHeight("450px")
        servicesContainer.setSizeUndefined()
        panel.setContent(servicesContainer)
        rootLayout.addComponent(panel)
        setCompositionRoot(rootLayout)
        search("")
    }

    private void add() {
        form.setService(new Service())
        form.openInModalPopup()
    }

    private void search(String serviceName) {
        servicesContainer.removeAllComponents()
        List<Service> services = serviceInteractor
                .findAllByProviderMatchingName(loggedUser, serviceName)
        populateList(services)
    }

    private void populateList(List<Service> services) {
        services.stream().forEach { service -> addServiceToContainer((Service) service) }
    }

    void addServiceToContainer(Service service) {
        MyServiceComponent myServiceComponent = new MyServiceComponent(service, loggedUser,
                serviceInteractor, form, this)
        servicesContainer.addComponent(myServiceComponent)
    }

    void updateList() {
        servicesContainer.removeAllComponents()
        List<Service> services = serviceInteractor.findAllByProviderId(loggedUser.getId())
        populateList(services)
    }

    @EventBusListenerMethod(scope = EventScope.SESSION)
    void onServiceCreatedEvent(ServiceCreatedEvent event) {
        search("")
        form.closePopup()
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
