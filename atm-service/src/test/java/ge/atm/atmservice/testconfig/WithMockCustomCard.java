package ge.atm.atmservice.testconfig;

import ge.atm.atmservice.security.jwt.JwtCardDetails;
import ge.atm.bankservice.domain.dto.CardDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomCardSecurityContextFactory.class)
public @interface WithMockCustomCard {

    String cardNumber() default "1111222211112222";

    String[] roles() default {};
}

class WithMockCustomCardSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomCard> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomCard customCard) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        List<SimpleGrantedAuthority> roles = Arrays.stream(customCard.roles())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        JwtCardDetails principal = new JwtCardDetails(customCard.cardNumber(), CardDto.PreferredAuthEnum.PIN, roles);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}