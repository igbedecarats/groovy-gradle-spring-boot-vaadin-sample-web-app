package app.ui.events

import app.quotations.domain.Quotation

class QuotationModifiedEvent implements Serializable {

    private Quotation quotation

    QuotationModifiedEvent(final Quotation quotation) {
        this.quotation = quotation
    }

    Quotation getQuotation() {
        quotation
    }
}