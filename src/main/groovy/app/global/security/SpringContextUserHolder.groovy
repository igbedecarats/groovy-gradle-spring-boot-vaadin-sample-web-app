package app.global.security

import app.users.domain.User
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

class SpringContextUserHolder {

    static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
        return (User) authentication.getPrincipal()
    }
}
