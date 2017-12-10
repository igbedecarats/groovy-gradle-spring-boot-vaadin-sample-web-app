package app.ui.events

import app.feedbacks.domain.Feedback

class FeedbackSubmittedEvent implements Serializable {

    private Feedback feedback

    FeedbackSubmittedEvent(final Feedback feedback) {
        this.feedback = feedback
    }

    Feedback getFeedback() {
        feedback
    }
}
