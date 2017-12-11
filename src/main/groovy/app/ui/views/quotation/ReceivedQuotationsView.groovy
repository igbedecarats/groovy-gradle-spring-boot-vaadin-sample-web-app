package app.ui.views.quotation

import app.global.security.UserHolder
import app.quotations.domain.Quotation
import app.quotations.quotation.QuotationInteractor
import app.ui.Sections
import app.users.domain.User
import app.users.usecase.UserInteractor
import com.vaadin.data.ValueProvider
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.spring.annotation.SpringView
import com.vaadin.ui.*
import com.vaadin.ui.renderers.ButtonRenderer
import com.vaadin.ui.renderers.ClickableRenderer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem

@Secured("ROLE_PROVIDER")
@SpringView(name = "myquotations")
@SideBarItem(sectionId = Sections.SERVICES, caption = "Pedidos Recibidos", order = 1)
@FontAwesomeIcon(FontAwesome.COMMENTS_O)
class ReceivedQuotationsView extends CustomComponent implements View {

    private Grid<Quotation> grid = new Grid<>()

    private QuotationInteractor quotationInteractor

    private UserInteractor userInteractor

    private UserHolder userHolder

    private User loggedUser

    @Autowired
    ReceivedQuotationsView(QuotationInteractor quotationInteractor,
                           UserInteractor userInteractor,
                           UserHolder userHolder) {
        this.quotationInteractor = quotationInteractor
        this.userInteractor = userInteractor
        this.userHolder = userHolder
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        loggedUser = this.userHolder.getUser()

        final VerticalLayout layout = new VerticalLayout()

        grid.addColumn(
                new ValueProvider<Quotation, String>() {
                    @Override
                    String apply(final Quotation quotation) {
                        quotation.getService().getName()
                    }
                }).setCaption("Servicio")
        grid.addColumn(
                new ValueProvider<Quotation, String>() {
                    @Override
                    String apply(final Quotation quotation) {
                        quotation.getClient().getFirstName() + " " + quotation.getClient()
                                .getLastName()
                    }
                }).setCaption("Cliente")
        grid.addColumn(
                new ValueProvider<Quotation, Float>() {
                    @Override
                    Float apply(final Quotation quotation) {
                        userInteractor.calculateUserRating(quotation.getClient()).getRating()
                    }
                }).setCaption("Calificacion")
        grid.addColumn(
                new ValueProvider<Quotation, String>() {
                    @Override
                    String apply(final Quotation quotation) {
                        quotation.getDescription()
                    }
                }).setCaption("Consideraciones")
        grid.addColumn(
                new ValueProvider<Quotation, String>() {
                    @Override
                    String apply(final Quotation quotation) {
                        quotation.getScheduledTime().toString()
                    }
                }).setCaption("Tiempo Agendado")
        grid.addColumn(
                new ValueProvider<Quotation, String>() {
                    @Override
                    String apply(final Quotation quotation) {
                        quotation.getStatus().getValue()
                    }
                }).setCaption("Estado")
        grid.addColumn("Aceptar",
                new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
                    @Override
                    void click(final ClickableRenderer.RendererClickEvent clickEvent) {
                        approve(clickEvent.getItem())
                        updateGrid()
                    }
                }))
        grid.addColumn("Rechazar",
                new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
                    @Override
                    void click(final ClickableRenderer.RendererClickEvent clickEvent) {
                        decline(clickEvent.getItem())
                        updateGrid()
                    }
                }))

        HorizontalLayout main = new HorizontalLayout(grid)
        main.setSizeFull()
        grid.setSizeFull()
        main.setExpandRatio(grid, 1)

        layout.addComponent(main)
        setCompositionRoot(layout)

        updateGrid()
    }

    private void decline(Object item) {
        try {
            Quotation quotation = (Quotation) item
            if (quotation.isCreated()) {
                quotationInteractor.decline(quotation)
                Notification.show("Éxito!", Notification.Type.HUMANIZED_MESSAGE)
            } else {
                Notification
                        .show("Quotation already marked as " + quotation.getStatus(), Notification.Type.WARNING_MESSAGE)
            }
        } catch (Exception ignored) {
            Notification
                    .show("Unable to process request, please contact the system admin", Notification.Type.ERROR_MESSAGE)
        }
    }

    private void approve(Object item) {
        try {
            Quotation quotation = (Quotation) item
            if (quotation.isCreated()) {
                quotationInteractor.approve(quotation)
                Notification.show("Éxito!", Notification.Type.HUMANIZED_MESSAGE)
            } else {
                Notification
                        .show("Quotation already marked as " + quotation.getStatus(), Notification.Type.WARNING_MESSAGE)
            }
        } catch (Exception ignored) {
            Notification
                    .show("Unable to process request, please contact the system admin", Notification.Type.ERROR_MESSAGE)
        }
    }

    private void updateGrid() {
        grid.setItems(quotationInteractor.findByProvider(loggedUser))
    }
}
