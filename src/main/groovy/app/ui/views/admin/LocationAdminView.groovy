package app.ui.views.admin

import app.locations.domain.Location
import app.locations.usecase.LocationInteractor
import app.ui.Sections
import com.vaadin.data.HasValue
import com.vaadin.data.ValueProvider
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.spring.annotation.SpringView
import com.vaadin.ui.*
import org.jsoup.helper.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem

@Secured("ROLE_ADMIN")
@SpringView(name = "locations")
@SideBarItem(sectionId = Sections.ADMIN, caption = "Ubicaciones", order = 1)
@FontAwesomeIcon(FontAwesome.MAP_MARKER)
class LocationAdminView extends CustomComponent implements View {

    private LocationInteractor locationInteractor

    private Grid<Location> grid = new Grid<>()

    private LocationAdminForm form

    @Autowired
    LocationAdminView(final LocationInteractor locationInteractor) {
        Validate.notNull(locationInteractor, "The Location Interactor cannot be null.")
        this.locationInteractor = locationInteractor
        form = new LocationAdminForm(locationInteractor, this)

        final VerticalLayout layout = new VerticalLayout()

        Button addLocationBtn = new Button("Agregar Nueva Ubicación")
        addLocationBtn.addClickListener(new Button.ClickListener() {

            @Override
            void buttonClick(final Button.ClickEvent event) {
                grid.asSingleSelect().clear()
                form.setLocation(new Location())
            }
        })

        HorizontalLayout toolbar = new HorizontalLayout(addLocationBtn)

        grid.addColumn(new ValueProvider() {

            @Override
            Object apply(final Object o) {
                return ((Location)o).getId()
            }
        }).setCaption("Id")
        grid.addColumn(new ValueProvider() {

            @Override
            Object apply(final Object o) {
                return ((Location)o).getName()
            }
        }).setCaption("Nombre")
        grid.addColumn(new ValueProvider<Location, String>() {
            @Override
            String apply(final Location location) {
                location.getArea()
            }
        }).setCaption("Area")
        grid.addColumn(new ValueProvider<Location, Double>() {
            @Override
            Double apply(final Location location) {
                location.getLatitude()
            }
        }).setCaption("Latitud")
        grid.addColumn(new ValueProvider<Location, Double>() {
            @Override
            Double apply(final Location location) {
                location.getLongitude()
            }
        }).setCaption("Longitud")

        HorizontalLayout main = new HorizontalLayout(grid, form)
        main.setSizeFull()
        grid.setSizeFull()
        main.setExpandRatio(grid, 1)

        layout.addComponents(toolbar, main)

        updateList()

        setCompositionRoot(layout)

        form.setVisible(false)

        grid.asSingleSelect().addValueChangeListener(
                new HasValue.ValueChangeListener() {

                    @Override
                    void valueChange(final HasValue.ValueChangeEvent event) {
                        if (event.getValue() == null) {
                            form.setVisible(false)
                        } else {
                            form.setLocation((Location)event.getValue())
                        }
                    }
                })
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

    void updateList() {
        List<Location> locations = locationInteractor.findAll()
        grid.setItems(locations)
    }
}
