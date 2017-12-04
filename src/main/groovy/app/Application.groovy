package app

import groovy.transform.CompileStatic
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan
import org.vaadin.spring.annotation.EnableVaadinExtensions
import org.vaadin.spring.events.annotation.EnableEventBus
import org.vaadin.spring.i18n.annotation.EnableI18N
import org.vaadin.spring.security.annotation.EnableVaadinManagedSecurity
import org.vaadin.spring.sidebar.annotation.EnableSideBar

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
@EnableVaadinManagedSecurity
@EnableVaadinExtensions
@EnableEventBus
@EnableSideBar
@EnableI18N
@ComponentScan
class Application extends SpringBootServletInitializer {

    static void main(String[] args) throws Exception {
        def app = new SpringApplication(Application)
        // further config...
        app.run(args)
    }

}
