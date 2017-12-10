package app.contracts

import app.contracts.domain.ContractRepository
import app.contracts.usecase.ContractInteractor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ContractConfig {

    @Autowired
    private ContractRepository contractRepository

    @Bean
    ContractInteractor contractInteractor() {
        return new ContractInteractor(contractRepository)
    }
}
