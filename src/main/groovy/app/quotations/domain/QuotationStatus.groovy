package app.quotations.domain

enum QuotationStatus {

    CREATED("Enviado"), APPROVED("Aprovado"), DECLINED("Rechazado")

    private final value

    def getValue() {
        return value
    }

    QuotationStatus(String value) {
        this.value = value
    }

}