package app.ui.views.search

import app.services.domain.Service
import app.services.usecase.ServiceInteractor
import app.ui.views.quotation.QuotationForm
import app.ui.views.service.AbstractServiceComponent
import app.users.domain.User
import com.vaadin.icons.VaadinIcons
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout

class SearchServiceComponent extends AbstractServiceComponent {

    private Button send = new Button()

    private QuotationForm form

    private User loggedUser

    SearchServiceComponent(final Service service, User loggedUser,
                           ServiceInteractor serviceInteractor, QuotationForm quotationForm) {
        super(service, loggedUser, serviceInteractor)
        this.loggedUser = loggedUser

        this.form = quotationForm
        HorizontalLayout buttonsContainer = new HorizontalLayout()
        send.setIcon(VaadinIcons.PAPERPLANE_O)
        send.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(final Button.ClickEvent event) {
                send()
            }
        })
        buttonsContainer.addComponents(send)
        buttonsContainer.setComponentAlignment(send, Alignment.BOTTOM_LEFT)
        addButtonsToProviderContainer(buttonsContainer)

        setSizeUndefined()
    }

    private void send() {
        form.show(service, loggedUser)
        form.openInModalPopup()
    }
}
