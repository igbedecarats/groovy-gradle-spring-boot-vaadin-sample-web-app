package app.ui.views.feedback

import app.feedbacks.domain.Feedback
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class FeedbackComponent extends CustomComponent {

    private Label providerName = new Label()
    private Label clientName = new Label()
    private Label creationTime = new Label()
    private Label comment = new Label()
    private Label rating = new Label()

    private VerticalLayout rootLayout = new VerticalLayout()
    
    FeedbackComponent(Feedback feedback) {
        setCompositionRoot(rootLayout)
        rootLayout.setSizeUndefined()
        providerName
                .setValue(feedback.getSender().getFirstName() + " " + feedback.getSender().getLastName())
        providerName.setCaption("De")
        clientName.setValue(
                feedback.getRecipient().getFirstName() + " " + feedback.getRecipient().getLastName())
        clientName.setCaption("Para")
        creationTime.setValue(feedback.getCreationTime().toString())
        creationTime.setCaption("Enviado")
        comment.setValue(feedback.getComment())
        comment.setCaption("Commentario")
        rating.setValue(Integer.toString(feedback.getRating()))
        rating.setCaption("Valoración")
        HorizontalLayout horizontalLayout = new HorizontalLayout()
        horizontalLayout.setSizeUndefined()
        horizontalLayout
                .addComponentsAndExpand(providerName, clientName, creationTime, rating, comment)
        rootLayout.addComponent(horizontalLayout)
    }

}
