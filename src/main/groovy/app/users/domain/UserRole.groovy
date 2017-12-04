package app.users.domain

import org.springframework.security.core.GrantedAuthority

enum UserRole implements GrantedAuthority {

    CLIENT, PROVIDER, ADMIN

    @Override
    String getAuthority() {
        return "ROLE_" + name()
    }
}
