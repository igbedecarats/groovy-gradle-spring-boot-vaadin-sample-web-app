package app.ui.views.admin

import app.global.security.UserHolder
import app.locations.usecase.LocationInteractor
import app.ui.Sections
import app.ui.views.global.UserForm
import app.users.domain.User
import app.users.usecase.UserInteractor
import com.vaadin.data.HasValue
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

//@Secured("ROLE_ADMIN")
//@SpringView(name = "users")
//@SideBarItem(sectionId = Sections.ADMIN, caption = "Usuarios", order = 0)
//@FontAwesomeIcon(FontAwesome.USERS)
class UserAdminView extends CustomComponent implements View {

    private UserInteractor userInteractor

    private LocationInteractor locationInteractor

    private UserHolder userHolder

    private Grid<User> grid = new Grid<>(User.class)

    private UserForm form

    @Autowired
    UserAdminView(final UserInteractor userInteractor,
                  final LocationInteractor locationInteractor, final UserHolder userHolder) {
        Validate.notNull(userInteractor, "The User Interactor cannot be null.")
        Validate.notNull(locationInteractor, "The Location Interactor cannot be null.")
        this.userInteractor = userInteractor
        this.locationInteractor = locationInteractor
        this.userHolder = userHolder
        User loggedUser = userHolder.getUser()
        form = new UserForm(loggedUser, this.userInteractor, this.locationInteractor,
                new Runnable() {
                    @Override
                    void run() {
                        updateList()
                    }
                })

        final VerticalLayout layout = new VerticalLayout()

        Button addUserBtn = new Button("Agregar nuevo usuario")
        addUserBtn.addClickListener(
                new Button.ClickListener() {
                    @Override
                    void buttonClick(final Button.ClickEvent event) {
                        grid.asSingleSelect().clear()
                        form.setUser(new User())
                    }
                })

        HorizontalLayout toolbar = new HorizontalLayout(addUserBtn)

        grid.setColumns("id", "username", "firstName", "lastName", "email")

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
                            form.setUser(event.getValue() as User)
                        }
                    }
                })
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

    void updateList() {
        List<User> users = userInteractor.findAll()
        grid.setItems(users)
    }
}
