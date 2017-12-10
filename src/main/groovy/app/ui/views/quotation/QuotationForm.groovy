package app.ui.views.quotation

import app.quotations.domain.Quotation
import app.quotations.quotation.QuotationInteractor
import app.services.domain.Service
import app.ui.events.QuotationModifiedEvent
import app.users.domain.User
import com.vaadin.spring.annotation.SpringComponent
import com.vaadin.spring.annotation.UIScope
import com.vaadin.ui.Component
import com.vaadin.ui.DateTimeField
import com.vaadin.ui.Notification
import com.vaadin.ui.TextArea
import com.vaadin.ui.themes.ValoTheme
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.spring.events.EventBus
import org.vaadin.viritin.form.AbstractForm
import org.vaadin.viritin.form.AbstractForm.SavedHandler
import org.vaadin.viritin.form.AbstractForm.ResetHandler
import org.vaadin.viritin.layouts.MFormLayout
import org.vaadin.viritin.layouts.MVerticalLayout

import java.time.LocalDateTime

@UIScope
@SpringComponent
class QuotationForm extends AbstractForm<Quotation> {

    private Service service
    private User loggedUser

    private QuotationInteractor quotationInteractor
    private final EventBus.SessionEventBus eventBus


    private DateTimeField scheduledTime = new DateTimeField("Agendadar para")
    private TextArea description = new TextArea("Consideraciones")

    @Autowired
    QuotationForm(QuotationInteractor quotationInteractor, EventBus.SessionEventBus eventBus) {
        super(Quotation.class)
        this.quotationInteractor = quotationInteractor
        this.eventBus = eventBus
        description.setStyleName(ValoTheme.TEXTAREA_LARGE)
        description.setWordWrap(true)
        description.setWidth("100%")
        scheduledTime.setValue(LocalDateTime.now())
        setSavedHandler(new SavedHandler<Quotation>() {
            @Override
            void onSave(final Quotation entity) {
                send()
            }
        })
        setResetHandler(new ResetHandler<Quotation>() {
            @Override
            void onReset(final Quotation quotation) {
                eventBus.publish(this, new QuotationModifiedEvent(quotation))
            }
        })
        setModalWindowTitle("Enviar Pedido")
        setSizeUndefined()
    }

    private void send() {
        try {
            Quotation quotation = quotationInteractor.create(description.getValue(), loggedUser, service, scheduledTime.getValue())
            // send the event for other parts of the application
            eventBus.publish(this, new QuotationModifiedEvent(quotation))
            Notification.show("Éxito!", Notification.Type.HUMANIZED_MESSAGE)
        } catch (Exception ignored) {
            Notification
                    .show("Unable to process request, please contact the system admin", Notification.Type.ERROR_MESSAGE)
        }
    }


    void show(Service service, User loggedUser) {
        setEntity(new Quotation())
        description.setValue(StringUtils.EMPTY)
        scheduledTime.setValue(LocalDateTime.now())
        this.service = service
        this.loggedUser = loggedUser
    }

    @Override
    protected Component createContent() {
        new MVerticalLayout(new MFormLayout(description, scheduledTime).withWidth(""),
                getToolbar()).withWidth("")
    }
}
