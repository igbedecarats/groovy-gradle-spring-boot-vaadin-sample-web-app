package app.global.security

import app.users.domain.User
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class UserHolder {

    Authentication authentication

    User getUser() {
        (User) authentication.getPrincipal()
    }

    void setAuthentication(AbstractAuthenticationToken authentication) {
        this.authentication = authentication
    }
}
