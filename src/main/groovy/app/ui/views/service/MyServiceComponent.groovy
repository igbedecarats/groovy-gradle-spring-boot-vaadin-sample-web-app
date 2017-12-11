package app.ui.views.service

import app.services.domain.Service
import app.services.usecase.ServiceInteractor
import app.users.domain.User
import com.vaadin.icons.VaadinIcons
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Notification
import org.apache.commons.lang3.Validate
import org.vaadin.viritin.button.ConfirmButton
import org.vaadin.viritin.button.MButton

class MyServiceComponent extends AbstractServiceComponent {

    private Button edit = new Button()
    private Button delete = new ConfirmButton(VaadinIcons.TRASH,
            "¿Estas seguro que queres borrar este servicio?",
            new MButton.MClickListener() {
                @Override
                void onClick() {
                    delete()
                }
            })

    private Service service
    private ServiceInteractor serviceInteractor
    private ServiceForm form
    private MyServicesView componentContainer

    MyServiceComponent(final Service service, User loggedUser,
                       final ServiceInteractor serviceInteractor,
                       final ServiceForm form, final MyServicesView componentContainer) {
        super(service, loggedUser, serviceInteractor)
        Validate.notNull(serviceInteractor, "The Service Interactor cannot be null.")
        Validate.notNull(form, "The form cannot be null.")
        Validate.notNull(componentContainer, "The Component Container cannot be null.")

        this.form = form
        this.service = service
        this.serviceInteractor = serviceInteractor
        this.componentContainer = componentContainer

        HorizontalLayout buttonsContainer = new HorizontalLayout()
        edit.setIcon(VaadinIcons.EDIT)
        edit.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(final Button.ClickEvent event) {
                edit()
            }
        })
        buttonsContainer.addComponents(edit, delete)
        addButtonsToProviderContainer(buttonsContainer)

        setSizeUndefined()
    }

    private void delete() {
        try {
            serviceInteractor.delete(service)
            componentContainer.updateList()
            Notification.show("Éxito!", Notification.Type.HUMANIZED_MESSAGE)
        } catch (Exception ignored) {
            Notification
                    .show("Unable to process request, please contact the system admin", Notification.Type.ERROR_MESSAGE)
        }
    }

    private void edit() {
        form.setService(service)
        form.openInModalPopup()
    }
}
