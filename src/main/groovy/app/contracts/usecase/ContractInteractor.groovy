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
        if (user == contract.getClient()) {
            contract.clientApproved()
        } else if (user.equals(contract.getService().getProvider())) {
            contract.providerApproved()
        } else {
            throw new IllegalArgumentException("User doesn't belong to contract")
        }
        save(contract)
    }
}
