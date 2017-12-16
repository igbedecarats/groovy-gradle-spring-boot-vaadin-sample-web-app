package app.ui.screens

import app.locations.domain.Location
import app.locations.domain.LocationArea
import app.locations.usecase.LocationInteractor
import app.ui.events.GoToLogin
import app.ui.events.GoToSignUp
import app.users.domain.User
import app.users.domain.UserRole
import app.users.usecase.UserInteractor
import com.vaadin.event.ShortcutAction
import com.vaadin.spring.annotation.SpringComponent
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.vaadin.spring.annotation.PrototypeScope
import org.vaadin.spring.events.EventBus
import org.vaadin.spring.security.VaadinSecurity
import org.vaadin.spring.security.util.SuccessfulLoginEvent

import java.util.stream.Collectors

@PrototypeScope
@SpringComponent
class SignUpScreen extends CustomComponent {

    private final VaadinSecurity vaadinSecurity
    private final EventBus.SessionEventBus eventBus
    private LocationInteractor locationInteractor
    private UserInteractor userInteractor

    private TextField username
    private TextField firstName
    private TextField lastName
    private TextField email
    private PasswordField password
    private CheckBox isProvider
    private List<String> existingLocations
    private NativeSelect<String> locations
    private Button goToLoginButton
    private Button signUpButton

    @Autowired
    SignUpScreen(VaadinSecurity vaadinSecurity, EventBus.SessionEventBus eventBus,
                 LocationInteractor locationInteractor, UserInteractor userInteractor) {
        this.vaadinSecurity = vaadinSecurity
        this.eventBus = eventBus
        this.locationInteractor = locationInteractor
        this.userInteractor = userInteractor
        initLayout()
    }

    private void initLayout() {
        username = new TextField("Usuario")
        firstName = new TextField("Nombre")
        lastName = new TextField("Apellido")
        email = new TextField("Email")
        password = new PasswordField("Password")
        isProvider = new CheckBox("Vas a publicar Servicios?")
        locations = new NativeSelect<>("Ubicacion")
        signUpButton = new Button("Registrarse")

        existingLocations = this.locationInteractor.findAll().stream()
                .map{ location -> ((Location)location)["name"] }.collect(
                Collectors.toList())
        locations.setItems(existingLocations)
        locations.setSelectedItem(existingLocations.stream().findFirst().get())
        locations.setEmptySelectionAllowed(false)

        FormLayout loginForm = new FormLayout()
        loginForm.setSizeUndefined()

        HorizontalLayout buttonsLayout = new HorizontalLayout()

        loginForm.addComponents(username, firstName, lastName, email, password, isProvider, locations,
                buttonsLayout)

        goToLoginButton = new Button("Volver")
        goToLoginButton.addStyleName(ValoTheme.BUTTON_LINK)
        goToLoginButton.setDisableOnClick(true)
        goToLoginButton.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(final Button.ClickEvent event) {
                goBack()
            }
        })
        signUpButton.addStyleName(ValoTheme.BUTTON_PRIMARY)
        signUpButton.setDisableOnClick(true)
        signUpButton.setClickShortcut(ShortcutAction.KeyCode.ENTER)
        signUpButton.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(final Button.ClickEvent event) {
                signUp()
            }
        })
        buttonsLayout.addComponentsAndExpand(goToLoginButton, signUpButton)

        VerticalLayout loginLayout = new VerticalLayout()
        loginLayout.setSizeUndefined()

        loginLayout.addComponent(loginForm)
        loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER)
        VerticalLayout rootLayout = new VerticalLayout(loginLayout)
        rootLayout.setSizeFull()
        rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER)
        setCompositionRoot(rootLayout)
        setSizeFull()
    }

    private void goBack() {
        eventBus.publish(this, new GoToLogin(getUI()))
    }

    private void signUp() {
        try {
            String passwordValue = password.getValue()
            password.setValue("")

            String locationName = locations.getSelectedItem()
                    .orElseThrow { -> new IllegalArgumentException("Please choose a locations") }
            Location location = locationInteractor.findByName(locationName)

            UserRole role
            if (isProvider.getValue()) {
                role = UserRole.PROVIDER
            } else {
                role = UserRole.CLIENT
            }

            User user = new User(username.getValue(), passwordValue, email.getValue(),
                    firstName.getValue(), lastName.getValue(), role)
            user.setLocation(location)
            user = userInteractor.save(user)

            final Authentication authentication = vaadinSecurity.login(user.getUsername(), passwordValue)
            eventBus.publish(this, new SuccessfulLoginEvent(getUI(), authentication))
        } catch (Exception ex) {
            username.focus()
            username.selectAll()
            Notification
                    .show(ex.getMessage(), Notification.Type.ERROR_MESSAGE)
            LoggerFactory.getLogger(getClass()).error(ex.getMessage(), ex)
        }
    }

}