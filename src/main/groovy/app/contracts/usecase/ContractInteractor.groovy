package app.contracts.usecase

import app.contracts.domain.Contract
import app.contracts.domain.ContractRepository
import app.users.domain.User

class ContractInteractor {

    private ContractRepository contractRepository

    ContractInteractor(ContractRepository contractRepository) {
        this.contractRepository = contractRepository
    }

    Contract save(Contract contract) {
        contractRepository.save(contract)
    }

    List<Contract> findForUser(User loggedUser) {
        contractRepository.findByClientIdOrServiceProviderId(loggedUser.getId(), loggedUser.getId())
    }

    void markAsDoneByUser(final Contract contract, final User user) {
        contract.markDoneByUser(user)
        save(contract)
    }
}
