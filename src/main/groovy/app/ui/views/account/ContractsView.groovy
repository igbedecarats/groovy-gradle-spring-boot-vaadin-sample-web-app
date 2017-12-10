package app.ui.views.account

import app.contracts.domain.Contract
import app.contracts.usecase.ContractInteractor
import app.global.security.UserHolder
import app.ui.Sections
import app.ui.events.ContractMarkedDoneEvent
import app.ui.events.FeedbackSubmittedEvent
import app.ui.views.feedback.FeedbackForm
import app.users.domain.User
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.spring.annotation.SpringView
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Panel
import com.vaadin.ui.VerticalLayout
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.spring.events.EventBus
import org.vaadin.spring.events.EventScope
import org.vaadin.spring.events.annotation.EventBusListenerMethod
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem

@SpringView(name = "contracts")
@SideBarItem(sectionId = Sections.ACCOUNT, caption = "Contratos", order = 0)
@FontAwesomeIcon(FontAwesome.SHOPPING_CART)
class ContractsView extends CustomComponent implements View {

    private ContractInteractor contractInteractor

    private VerticalLayout contractContainer = new VerticalLayout()

    private User loggedUser

    private final EventBus.SessionEventBus eventBus

    private FeedbackForm feedbackForm

    private UserHolder userHolder

    @Autowired
    ContractsView(ContractInteractor contractInteractor, EventBus.SessionEventBus eventBus,
                  FeedbackForm feedbackForm, final UserHolder userHolder) {
        this.contractInteractor = contractInteractor
        this.eventBus = eventBus
        this.eventBus.subscribe(this)
        this.feedbackForm = feedbackForm
        this.userHolder = userHolder
        loggedUser = userHolder.getUser()
        VerticalLayout rootLayout = new VerticalLayout()
        rootLayout.setSizeFull()
        Panel panel = new Panel("Contratos")
        panel.setWidth("1000px")
        panel.setHeight("600px")
        contractContainer.setSizeUndefined()
        panel.setContent(contractContainer)
        rootLayout.addComponent(panel)
        setCompositionRoot(rootLayout)
        updateList()
    }

    void updateList() {
        List<Contract> contracts = contractInteractor.findForUser(loggedUser)
        contractContainer.removeAllComponents()
        contracts.stream()
                .forEach { contract ->
            contractContainer
                    .addComponent(new ContractComponent(loggedUser, (Contract) contract, contractInteractor, eventBus,
                    feedbackForm))
        }
    }

    @EventBusListenerMethod(scope = EventScope.SESSION)
    void onContractMarkedDoneEvent(ContractMarkedDoneEvent event) {
        updateList()
    }

    @EventBusListenerMethod(scope = EventScope.SESSION)
    void onFeedbackSubmittedEvent(FeedbackSubmittedEvent event) {
        feedbackForm.closePopup()
        updateList()
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
