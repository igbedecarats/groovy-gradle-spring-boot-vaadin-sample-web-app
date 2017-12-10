package app.quotations.domain

import org.springframework.data.jpa.repository.JpaRepository

interface QuotationRepository extends JpaRepository<Quotation, Long> {

    List<Quotation> findByClientIdAndStatus(long id, QuotationStatus status)

    List<Quotation> findByServiceProviderIdAndStatus(long id, QuotationStatus status)
}