package app.users.domain

import app.locations.domain.Location
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.Validate
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder

import javax.persistence.*

@Entity
@Table(name = "user")
@JsonIgnoreProperties(ignoreUnknown = true)
class User {

    @Id
    @GeneratedValue
    long id

    @Column(name = "username", nullable = false, unique = true)
    String username

    @Column(name = "password", nullable = false)
    String password

    @Column(name = "first_name", nullable = false)
    String firstName

    @Column(name = "last_name", nullable = false)
    String lastName

    @Column(name = "email", nullable = false, unique = true)
    String email

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    UserRole role

    @ManyToOne
    @JoinColumn(name = "preferred_location_id")
    Location location

    User() {}

    User(
            final String username,
            final String password,
            final String firstName, final String lastName, final String email, final UserRole role) {
        this.username = username
        this.password = password
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.role = role
    }

    void setRole(final UserRole role) {
        Validate.notNull(role, "The UserRole cannot be null")
        this.role = role
    }

    void setLocation(final Location location) {
        this.location = location
    }

    def isAdmin() {
        return role.equals(UserRole.ADMIN)
    }

    def isClient() {
        return role.equals(UserRole.CLIENT)
    }

    def isProvider() {
        return role.equals(UserRole.PROVIDER)
    }

    @Override
    boolean equals(final Object o) {
        if (this == o) {
            return true
        }

        if (o == null || getClass() != o.getClass()) {
            return false
        }

        User user = (User) o

        return new EqualsBuilder().append(id, user.id).append(username, user.getUsername())
                .append(email, user.getEmail()).isEquals()
    }

    @Override
    int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(username).append(email).toHashCode()
    }

    @Override
    String toString() {
        return new ToStringBuilder(this).append("id", id).append("username", username)
                .append("email", email).append("firstName", firstName)
                .append("lastName", lastName).toString()
    }
}
