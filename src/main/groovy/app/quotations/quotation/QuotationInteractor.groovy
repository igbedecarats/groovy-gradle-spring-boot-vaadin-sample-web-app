package app.quotations.quotation

import app.contracts.domain.Contract
import app.contracts.usecase.ContractInteractor
import app.quotations.domain.Quotation
import app.quotations.domain.QuotationRepository
import app.quotations.domain.QuotationStatus
import app.services.domain.Service
import app.users.domain.User

import java.time.LocalDateTime

class QuotationInteractor {

    private QuotationRepository quotationRepository

    private ContractInteractor contractInteractor

    QuotationInteractor(QuotationRepository quotationRepository, ContractInteractor contractInteractor) {
        this.quotationRepository = quotationRepository
        this.contractInteractor = contractInteractor
    }

    Quotation create(String description, User client, Service service, LocalDateTime scheduledTime) {
        if (contractInteractor.hasAvailableTimeForServiceOn(service, scheduledTime)) {
            throw new IllegalArgumentException("Ya existe una reserva para ese momento seleccionado")
        }
        quotationRepository.save(new Quotation(description, client, service, scheduledTime))
    }

    List<Quotation> findByClient(User loggedUser) {
        quotationRepository.findByClientIdAndStatus(loggedUser.getId(), QuotationStatus.CREATED)
    }

    List<Quotation> findByProvider(User loggedUser) {
        quotationRepository.findByServiceProviderIdAndStatus(loggedUser.getId(), QuotationStatus.CREATED)
    }

    void decline(Quotation quotation) {
        quotation.setStatus(QuotationStatus.DECLINED)
        quotationRepository.save(quotation)
    }

    void approve(Quotation quotation) {
        quotation.setStatus(QuotationStatus.APPROVED)
        List<Quotation> quotations = Arrays.asList(quotation)
        Contract contract = new Contract(quotation.getClient(), quotation.getService(),
                quotation.getScheduledTime(),
                quotations)
        contractInteractor.save(contract)
        quotationRepository.save(quotation)
    }

    Quotation save(Quotation quotation) {
        quotationRepository.save(quotation)
    }
}
