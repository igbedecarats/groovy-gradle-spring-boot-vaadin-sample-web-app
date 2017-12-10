package app.quotations

import app.contracts.usecase.ContractInteractor
import app.quotations.domain.QuotationRepository
import app.quotations.quotation.QuotationInteractor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QuotationConfig {

    @Autowired
    private QuotationRepository quotationRepository

    @Autowired
    private ContractInteractor contractInteractor

    @Bean
    QuotationInteractor quotationInteractor() {
        new QuotationInteractor(quotationRepository, contractInteractor)
    }

}
