package app.ui.operations

import app.ui.Sections
import com.vaadin.server.FontAwesome
import com.vaadin.spring.annotation.SpringComponent
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.spring.security.managed.VaadinManagedSecurity
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem

@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "Salir")
@FontAwesomeIcon(FontAwesome.POWER_OFF)
class LogoutOperation implements Runnable {

    private final VaadinManagedSecurity vaadinSecurity;

    @Autowired
    public LogoutOperation(VaadinManagedSecurity vaadinSecurity) {
        this.vaadinSecurity = vaadinSecurity;
    }

    @Override
    public void run() {
        vaadinSecurity.logout("?goodbye");
    }
}
