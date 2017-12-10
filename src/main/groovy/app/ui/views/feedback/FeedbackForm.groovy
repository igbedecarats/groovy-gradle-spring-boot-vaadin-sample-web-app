package app.ui.views.feedback

import app.contracts.domain.Contract
import app.feedbacks.domain.Feedback
import app.feedbacks.usecase.FeedbackInteractor
import app.ui.events.FeedbackSubmittedEvent
import app.users.domain.User
import com.vaadin.ui.Component
import com.vaadin.ui.NativeSelect
import com.vaadin.ui.Notification
import com.vaadin.ui.TextArea
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.spring.events.EventBus
import org.vaadin.viritin.form.AbstractForm
import org.vaadin.viritin.form.AbstractForm.SavedHandler
import org.vaadin.viritin.form.AbstractForm.ResetHandler
import org.vaadin.viritin.layouts.MFormLayout
import org.vaadin.viritin.layouts.MVerticalLayout

class FeedbackForm extends AbstractForm<Feedback> {

    private Contract contract
    private User loggedUser
    private FeedbackInteractor feedbackInteractor

    private final EventBus.SessionEventBus eventBus

    private TextArea comment = new TextArea("Comentario")
    private NativeSelect<Integer> ratings = new NativeSelect<>("Calificación")

    @Autowired
    FeedbackForm(FeedbackInteractor feedbackInteractor, EventBus.SessionEventBus eventBus) {
        super(Feedback.class)
        this.feedbackInteractor = feedbackInteractor
        this.eventBus = eventBus
        ratings.setItems(Feedback.allowedRatings())
        ratings.setEmptySelectionAllowed(false)
        setSavedHandler(new SavedHandler<Feedback>() {
            @Override
            void onSave(final Feedback feedback) {
                send()
            }
        })
        setResetHandler(new ResetHandler<Feedback>() {
            @Override
            void onReset(final Feedback feedback) {
                eventBus.publish(this, new FeedbackSubmittedEvent(feedback))
            }
        })
        setModalWindowTitle("Enviar Evaluación")
        setSizeUndefined()
    }

    private void send() {
        try {
            Feedback feedback = feedbackInteractor
                    .submitByUser(loggedUser, contract, comment.getValue(), ratings
                    .getValue())
            // send the event for other parts of the application
            eventBus.publish(this, new FeedbackSubmittedEvent(feedback))
            Notification.show("Éxito!", Notification.Type.HUMANIZED_MESSAGE)
        } catch (Exception ignored) {
            Notification
                    .show("Unable to process request, please contact the system admin", Notification.Type.ERROR_MESSAGE)
        }
    }

    void show(Contract contract, User loggedUser) {
        setEntity(new Feedback())
        comment.setValue(StringUtils.EMPTY)
        ratings.setSelectedItem(Feedback.allowedRatings().stream().findFirst().get())
        this.contract = contract
        this.loggedUser = loggedUser
    }

    @Override
    protected Component createContent() {
        new MVerticalLayout(new MFormLayout(comment, ratings).withWidth(""),
                getToolbar()).withWidth("")
    }
}
