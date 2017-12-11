package app.services.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.EqualsAndHashCode
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder

import javax.persistence.*

@Entity
@Table(name = "service_sub_category")
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(includes='name')
class ServiceSubCategory {

    @Id
    @GeneratedValue
    private long id

    @Column(name = "name", nullable = false, unique = true)
    private String name

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ServiceCategory category

    ServiceSubCategory(final String name, final ServiceCategory category) {
        this.name = name
        this.category = category
    }

    ServiceSubCategory() {}

    long getId() {
        id
    }

    String getName() {
        name
    }

    ServiceCategory getCategory() {
        category
    }
}
