package app.users.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(final String username, final String password)

    User findByUsername(final String username)
}
