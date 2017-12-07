package app.ui.views.profile

import app.global.security.UserHolder
import app.locations.usecase.LocationInteractor
import app.ui.Sections
import app.ui.views.global.UserForm
import app.users.domain.User
import app.users.usecase.UserInteractor
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.spring.annotation.SpringView
import com.vaadin.ui.Alignment
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.VerticalLayout
import org.jsoup.helper.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem

@SpringView(name = "profile")
@SideBarItem(sectionId = Sections.PROFILE, caption = "Perfil")
@FontAwesomeIcon(FontAwesome.USER)
class ProfileView extends CustomComponent implements View {

    private UserInteractor userInteractor

    private LocationInteractor locationInteractor

    private UserHolder userHolder

    @Autowired
    ProfileView(final UserInteractor userInteractor,
                final LocationInteractor locationInteractor,
                final UserHolder userHolder) {
        Validate.notNull(userInteractor, "The User Interactor cannot be null.")
        Validate.notNull(locationInteractor, "The Location Interactor cannot be null.")
        this.userInteractor = userInteractor
        this.locationInteractor = locationInteractor
        this.userHolder = userHolder
        User loggedUser = userHolder.getUser()
        UserForm form = new UserForm(loggedUser, this.userInteractor, this.locationInteractor, null)
        form.setUser(loggedUser)
        VerticalLayout layout = new VerticalLayout()
        layout.setSizeUndefined()
        layout.addComponent(form)
        layout.setComponentAlignment(form, Alignment.TOP_CENTER)
        VerticalLayout rootLayout = new VerticalLayout(layout)
        rootLayout.setSizeFull()
        rootLayout.setComponentAlignment(layout, Alignment.MIDDLE_CENTER)
        setCompositionRoot(rootLayout)
        setSizeFull()
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
