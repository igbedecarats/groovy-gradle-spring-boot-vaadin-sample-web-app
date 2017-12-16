package app.ui.views.account

import app.contracts.domain.Contract
import app.contracts.usecase.ContractInteractor
import app.feedbacks.domain.Feedback
import app.global.date.DateUtils
import app.ui.events.ContractMarkedDoneEvent
import app.ui.views.feedback.FeedbackComponent
import app.ui.views.feedback.FeedbackForm
import app.users.domain.User
import com.vaadin.icons.VaadinIcons
import com.vaadin.ui.*
import org.vaadin.spring.events.EventBus
import org.vaadin.viritin.button.ConfirmButton
import org.vaadin.viritin.button.MButton

class ContractComponent extends CustomComponent {

    private FeedbackForm feedbackForm

    private Label serviceName = new Label()
    private Label providerName = new Label()
    private Label clientName = new Label()
    private Label scheduledTime = new Label()
    private Label status = new Label()
    private Button done = new ConfirmButton(VaadinIcons.CHECK,
            "¿Estas seguro que queres marcarla como terminada?",
            new MButton.MClickListener() {
                @Override
                void onClick() {
                    done()
                }
            })
    private Button send = new Button()

    private User loggedUser
    private Contract contract
    private ContractInteractor contractInteractor
    private EventBus.SessionEventBus eventBus

    private VerticalLayout root = new VerticalLayout()

    ContractComponent(User loggedUser, Contract contract,
                      ContractInteractor contractInteractor,
                      EventBus.SessionEventBus eventBus, FeedbackForm feedbackForm) {
        this.loggedUser = loggedUser
        this.contract = contract
        this.contractInteractor = contractInteractor
        this.eventBus = eventBus
        this.feedbackForm = feedbackForm

        serviceName.setValue(contract.getService().getName())
        serviceName.setCaption("Servicio")
        User provider = contract.getService().getProvider()
        providerName.setValue(provider.getFirstName() + " " + provider.getLastName())
        providerName.setVisible(!loggedUser.equals(provider))
        providerName.setCaption("Provedor")
        User client = contract.getClient()
        clientName.setValue(client.getFirstName() + " " + client.getLastName())
        clientName.setVisible(loggedUser != client)
        clientName.setCaption("Cliente")
        scheduledTime.setValue(DateUtils.convertToString(contract.getScheduledTime()))
        scheduledTime.setCaption("Tiempo Acordado")
        status.setValue(contract.getStatus().getValue())
        status.setCaption("Estado")
        done.setVisible(shouldShowDoneButton())
        send.setIcon(VaadinIcons.PAPERPLANE_O)
        send.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(final Button.ClickEvent event) {
                send()
            }
        })
        send.setVisible(contract.isCompleted() && !contract.feedbackAlreadyGivenByUser(loggedUser))
        HorizontalLayout labelsHorizontalLayout = new HorizontalLayout(providerName, clientName,
                scheduledTime, status)
        labelsHorizontalLayout.setSizeUndefined()
        HorizontalLayout buttonsHorizontalLayout = new HorizontalLayout(done, send)
        buttonsHorizontalLayout.setSizeUndefined()
        buttonsHorizontalLayout.setComponentAlignment(done, Alignment.MIDDLE_LEFT)
        buttonsHorizontalLayout.setComponentAlignment(send, Alignment.MIDDLE_LEFT)
        HorizontalLayout contractHorizontalLayout = new HorizontalLayout(labelsHorizontalLayout, buttonsHorizontalLayout)
        contractHorizontalLayout.setSizeFull()
        root.addComponentsAndExpand(contractHorizontalLayout)
        VerticalLayout feedbacksContainer = new VerticalLayout()
        List<Feedback> feedbacks = contract.getFeedbacks()
        feedbacks.stream()
                .forEach { feedback -> feedbacksContainer.addComponent(new FeedbackComponent((Feedback) feedback)) }
        root.addComponentsAndExpand(feedbacksContainer)
        root.setSizeUndefined()
        setCompositionRoot(root)
    }

    private boolean shouldShowDoneButton() {
        return (loggedUser == contract.getClient() && !contract.isClientApproved) ||
                (loggedUser == contract.getService().getProvider() && !contract.isProviderApproved)
    }

    private void send() {
        feedbackForm.show(contract, loggedUser)
        feedbackForm.openInModalPopup()
    }

    private void done() {
        try {
            contractInteractor.markAsDoneByUser(contract, loggedUser)
            Notification.show("Éxito!", Notification.Type.HUMANIZED_MESSAGE)
            eventBus.publish(this, new ContractMarkedDoneEvent(contract))
        } catch (Exception ignored) {
            Notification
                    .show("Unable to process request, please contact the system admin", Notification.Type.ERROR_MESSAGE)
        }
    }
}
