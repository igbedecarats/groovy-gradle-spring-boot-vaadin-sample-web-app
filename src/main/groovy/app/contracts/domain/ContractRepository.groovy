package app.contracts.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import java.time.LocalDateTime

interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findByClientIdOrServiceProviderId(long id, long id1)

    @Query("select c from Contract c where c.service.id = ?1 and status != ?2 and scheduledTime = ?3")
    List<Contract> findByServicIdAndStatusAndScheduledTime(long id, ContractStatus status, LocalDateTime scheduledTime)
}