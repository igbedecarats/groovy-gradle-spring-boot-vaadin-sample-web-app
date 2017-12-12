package app.global.security

import app.users.domain.User
import app.users.domain.UserRepository
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserAuthenticationProvider implements AuthenticationProvider {

    private UserRepository userRepository

    private UserHolder holder

    UserAuthenticationProvider(final UserRepository userRepository, final UserHolder holder) {
        this.userRepository = userRepository
        this.holder = holder
    }

    @Override
    Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) authentication
        final String username = (String) usernamePasswordAuthenticationToken.getPrincipal()
        final String password = (String) usernamePasswordAuthenticationToken.getCredentials()
        User user = userRepository.findByUsername(username)
        if (user != null && user.getPassword().equals(password)) {
            AbstractAuthenticationToken abstractAuthenticationToken = getAbstractAuthenticationToken(
                    user)
            abstractAuthenticationToken.setAuthenticated(true)
            SecurityContextHolder.getContext().setAuthentication(abstractAuthenticationToken)
            holder.setAuthentication(abstractAuthenticationToken)
            return abstractAuthenticationToken
        }
        throw new UsernameNotFoundException(username)
    }

    private AbstractAuthenticationToken getAbstractAuthenticationToken(User user) {
        return new AbstractAuthenticationToken(Arrays.asList(user.getRole())) {
            @Override
            Object getCredentials() {
                return user.getPassword()
            }

            @Override
            Object getPrincipal() {
                return user
            }
        }
    }

    @Override
    boolean supports(Class<?> authentication) {
        UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
    }
}
