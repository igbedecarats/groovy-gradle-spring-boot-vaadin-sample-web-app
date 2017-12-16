package app.contracts.usecase

import app.contracts.domain.Contract
import app.contracts.domain.ContractRepository
import app.contracts.domain.ContractStatus
import app.services.domain.Service
import app.users.domain.User

import java.time.LocalDateTime

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

    boolean hasAvailableTimeForServiceOn(Service service, LocalDateTime scheduledTime) {
        contractRepository.findByServicIdAndStatusAndScheduledTime(service.getId(), ContractStatus.COMPLETED,
                scheduledTime).stream().any()
    }
}
