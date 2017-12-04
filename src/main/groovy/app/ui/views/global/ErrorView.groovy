package app.ui.views.global

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

class ErrorView extends VerticalLayout implements View {

    private Label message

    ErrorView() {
        setMargin(true)
        addComponent(message = new Label())
        message.setSizeUndefined()
        message.addStyleName(ValoTheme.LABEL_FAILURE)
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        message.setValue(String.format("No such view: %s", event.getViewName()))
    }
}
