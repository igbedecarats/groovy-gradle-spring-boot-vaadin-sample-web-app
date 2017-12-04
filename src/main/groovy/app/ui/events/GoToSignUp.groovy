package app.ui.events

import com.vaadin.ui.UI
import org.springframework.context.ApplicationEvent

class GoToSignUp extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    GoToSignUp(UI source) {
        super(source)
    }

    @Override
    UI getSource() {
        return (UI) super.getSource()
    }
}
