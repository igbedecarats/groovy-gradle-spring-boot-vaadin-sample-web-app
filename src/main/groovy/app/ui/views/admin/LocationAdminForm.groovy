package app.ui.views.admin

import app.locations.domain.Location
import app.locations.domain.LocationArea
import app.locations.usecase.LocationInteractor
import com.vaadin.data.Binder
import com.vaadin.event.ShortcutAction
import com.vaadin.icons.VaadinIcons
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import org.vaadin.viritin.button.ConfirmButton
import org.vaadin.viritin.button.MButton

class LocationAdminForm extends FormLayout {

    private LocationInteractor locationInteractor
    private LocationAdminView formContainer
    private Location location
    private Binder<Location> binder = new Binder<>(Location.class)

    private TextField name = new TextField("Nombre")
    private NativeSelect<LocationArea> area = new NativeSelect<>("Area")
    private Label latitude = new Label()
    private Label longitude = new Label()
    private Button save = new Button("Guardar")
    private Button delete = new ConfirmButton(VaadinIcons.TRASH,
            "Estas seguro que queres borrarlo?", new MButton.MClickListener() {
        @Override
        void onClick() {
            delete()
        }
    })

    LocationAdminForm(LocationInteractor locationInteractor,
                      LocationAdminView formContainer) {
        this.locationInteractor = locationInteractor
        this.formContainer = formContainer
        setSizeUndefined()
        HorizontalLayout buttons = new HorizontalLayout(save, delete)
        addComponents(name, area, latitude, longitude, buttons)
        area.setItems(LocationArea.values())
        save.setStyleName(ValoTheme.BUTTON_PRIMARY)
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER)
        binder.bindInstanceFields(this)
        save.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(final Button.ClickEvent event) {
                save()
            }
        })
    }

    private void delete() {
        try {
            locationInteractor.delete(location)
            formContainer.updateList()
            setVisible(false)
            Notification.show("Éxito!", Notification.Type.HUMANIZED_MESSAGE)
        } catch (Exception e) {
            Notification
                    .show("Unable to process request, please contact the system admin", Notification.Type.ERROR_MESSAGE)
        }
    }

    private void save() {
        try {
            locationInteractor.save(location)
            formContainer.updateList()
            setVisible(false)
            Notification.show("Éxito!", Notification.Type.HUMANIZED_MESSAGE)
        } catch (Exception e) {
            Notification
                    .show("Unable to process request, please contact the system admin", Notification.Type.ERROR_MESSAGE)
        }
    }

    void setLocation(Location location) {
        this.location = location
        binder.setBean(location)
        setVisible(true)
        latitude.setValue(Double.toString(location.getLatitude()))
        longitude.setValue(Double.toString(location.getLongitude()))
        if (location.getId() == 0) {
            name.selectAll()
            name.setEnabled(true)
        } else {
            name.setEnabled(false)
        }
    }
}
