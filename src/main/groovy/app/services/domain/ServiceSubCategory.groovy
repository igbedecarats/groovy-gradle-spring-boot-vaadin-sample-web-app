package app.services.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder

import javax.persistence.*

@Entity
@Table(name = "service_sub_category")
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Override
    boolean equals(final Object o) {
        if (this == o) {
            true
        }

        if (o == null || getClass() != o.getClass()) {
            false
        }

        ServiceSubCategory subCategory = (ServiceSubCategory) o

        new EqualsBuilder().append(name, subCategory.name).isEquals()
    }

    @Override
    int hashCode() {
        new HashCodeBuilder(17, 37).append(name).toHashCode()
    }

    @Override
    String toString() {
        new ToStringBuilder(this).append("id", id).append("name", name).toString()
    }
}
