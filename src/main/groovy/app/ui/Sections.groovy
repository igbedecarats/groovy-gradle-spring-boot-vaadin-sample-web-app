package app.ui

import org.springframework.stereotype.Component
import org.vaadin.spring.sidebar.annotation.SideBarSection
import org.vaadin.spring.sidebar.annotation.SideBarSections

@Component
@SideBarSections([
        @SideBarSection(id = Sections.SEARCH),
        @SideBarSection(id = Sections.ACCOUNT, caption = "Cuenta"),
        @SideBarSection(id = Sections.SERVICES, caption = "Servicios"),
        @SideBarSection(id = Sections.PROFILE, caption = "Perfil"),
        @SideBarSection(id = Sections.ADMIN, caption = "Admin"),
        @SideBarSection(id = Sections.OPERATIONS)
])
class Sections {

    public static final String SEARCH = "search"
    public static final String ACCOUNT = "account"
    public static final String SERVICES = "services"
    public static final String PROFILE = "profile"
    public static final String ADMIN = "admin"
    public static final String OPERATIONS = "operations"
}
