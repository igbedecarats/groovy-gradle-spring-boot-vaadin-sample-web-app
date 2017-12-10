package app.contracts.domain

import org.springframework.data.jpa.repository.JpaRepository

interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findByClientIdOrServiceProviderId(long id, long id1)
}