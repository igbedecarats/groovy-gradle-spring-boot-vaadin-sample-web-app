package app.contracts.domain

enum ContractStatus {

    IN_PROGRESS("En Progreso"), COMPLETED("Completado"), DONE_BY_CLIENT(
            "Aprovado por el Cliente"), DONE_BY_PROVIDER("Aprovado por el Proveedor")

    private final String value

    String getValue() {
        return value
    }

    ContractStatus(String value) {
        this.value = value
    }
}
