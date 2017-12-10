package app.ui.events

import app.contracts.domain.Contract

class ContractMarkedDoneEvent implements Serializable {

    private Contract contract

    ContractMarkedDoneEvent(final Contract contract) {
        this.contract = contract
    }

    Contract getContract() {
        contract
    }
}
