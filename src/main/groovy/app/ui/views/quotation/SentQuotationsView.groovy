package app.ui.views.quotation

import app.global.security.UserHolder
import app.quotations.domain.Quotation
import app.quotations.quotation.QuotationInteractor
import app.ui.Sections
import app.users.domain.User
import com.vaadin.data.ValueProvider
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.spring.annotation.SpringView
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Grid
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.VerticalLayout
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem

@SpringView(name = "quotations")
@SideBarItem(sectionId = Sections.ACCOUNT, caption = "Pedidos Enviados", order = 1)
@FontAwesomeIcon(FontAwesome.COMMENTS)
class SentQuotationsView extends CustomComponent implements View {

    private Grid<Quotation> grid = new Grid<>()

    private QuotationInteractor quotationInteractor

    private UserHolder userHolder

    private User loggedUser

    @Autowired
    SentQuotationsView(QuotationInteractor quotationInteractor, UserHolder userHolder) {
        this.quotationInteractor = quotationInteractor
        this.userHolder = userHolder
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        loggedUser = userHolder.getUser()

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

        HorizontalLayout main = new HorizontalLayout(grid)
        main.setSizeFull()
        grid.setSizeFull()
        main.setExpandRatio(grid, 1)

        layout.addComponent(main)
        setCompositionRoot(layout)

        grid.setItems(quotationInteractor.findByClient(loggedUser))
    }
}
