package app.ui.views.global

import app.locations.domain.Location
import app.locations.domain.LocationArea
import app.locations.usecase.LocationInteractor
import app.users.domain.User
import app.users.domain.UserRole
import app.users.usecase.usecase.UserInteractor
import com.vaadin.data.Binder
import com.vaadin.event.ShortcutAction
import com.vaadin.icons.VaadinIcons
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import org.vaadin.viritin.button.ConfirmButton

import java.util.stream.Collectors

class UserForm extends FormLayout {

    private UserInteractor userInteractor
    private LocationInteractor locationInteractor
    private Runnable function
    private Binder<User> binder = new Binder<>(User.class)
    private User user
    private User loggedUser

    private TextField username = new TextField("Usuario")
    private TextField firstName = new TextField("Nombre")
    private TextField lastName = new TextField("Apellido")
    private TextField email = new TextField("Email")
    private TextField password = new TextField("Password")
    private NativeSelect<UserRole> role = new NativeSelect<>("Rol")
    private final List<String> existingLocations
    private NativeSelect<String> locations = new NativeSelect<>("Ubicacion")
    private Button save = new Button("Guardar")
    private Button delete = new ConfirmButton(VaadinIcons.TRASH,
            "¿Estas seguro que queres borrar este usuario?",
            new Button.ClickListener() {
                @Override
                void buttonClick(final Button.ClickEvent event) {
                    delete()
                }
            })

    UserForm(User loggedUser, UserInteractor userInteractor,
             LocationInteractor locationInteractor, Runnable function) {
        this.loggedUser = loggedUser
        this.userInteractor = userInteractor
        this.locationInteractor = locationInteractor
        this.function = function
        setSizeUndefined()
        HorizontalLayout buttons = new HorizontalLayout(save, delete)
        addComponents(username, firstName, lastName, email, password, role, locations, buttons)
        if (loggedUser.isAdmin()) {
            role.setItems(Arrays.asList(UserRole.values()))
        } else {
            role.setItems(Arrays.asList(UserRole.CLIENT, UserRole.PROVIDER))
        }
        role.setSelectedItem(UserRole.CLIENT)
        role.setEmptySelectionAllowed(false)
        existingLocations = this.locationInteractor.findAll().stream()
                .map { location -> ((Location) location)["name"] }
                .collect(Collectors.toList())
        locations.setItems(existingLocations)
        locations.setSelectedItem(existingLocations.stream().findFirst().get())
        locations.setEmptySelectionAllowed(false)
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
            userInteractor.delete(user)
            if (function != null) {
                function.run()
            }
            Notification.show("Éxito!", Notification.Type.HUMANIZED_MESSAGE)
            setVisible(false)
        } catch (Exception e) {
            Notification
                    .show("Unable to process request, please contact the system admin", Notification.Type.ERROR_MESSAGE)
        }
    }

    void setUser(final User user) {
        this.user = user
        binder.setBean(user)
        Location location = user.getLocation()
        if (location != null) {
            locations.setSelectedItem(location.getName())
        } else {
            locations.setSelectedItem(existingLocations.stream().findFirst().get())
        }
        if (user.getRole() == null) {
            role.setSelectedItem(UserRole.CLIENT)
        }
        // Show delete button for only users already in the database
        delete
                .setVisible(user.getId() != 0 && isLoggedUserNotSelectedUser(user) && loggedUser.isAdmin())
        setVisible(true)
        username.selectAll()
        username.setEnabled(isLoggedUserNotSelectedUser(user) && loggedUser.isAdmin())
        password.setEnabled(isLoggedUserNotSelectedUser(user) && loggedUser.isAdmin())
    }

    private boolean isLoggedUserNotSelectedUser(User user) {
        return loggedUser.getId() != user.getId()
    }

    private void save() {
        try {
            Location location = locationInteractor.findByName(locations.getSelectedItem().get())
            user.setLocation(location)
            userInteractor.save(user)
            if (function != null) {
                function.run()
                setVisible(false)
            }
            Notification
                    .show("Éxito!", Notification.Type.HUMANIZED_MESSAGE)
        } catch (Exception e) {
            Notification
                    .show("Unable to process request, please contact the system admin", Notification.Type.ERROR_MESSAGE)
        }
    }
}
