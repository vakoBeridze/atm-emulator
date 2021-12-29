package ge.atm.atmservice.security;

import ge.atm.atmservice.service.CardService;
import ge.atm.bankservice.domain.dto.CardDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CardAuthenticationProvider implements AuthenticationProvider {

    private final CardService cardService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String cardNumber = authentication.getName();
        final CardDto cardDto = cardService.getCard(cardNumber);
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(cardDto.getCardNumber(), null);
        authenticationToken.setDetails(cardDto.getPreferredAuth());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
