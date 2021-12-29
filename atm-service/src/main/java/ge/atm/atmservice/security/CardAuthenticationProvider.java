package ge.atm.atmservice.security;

import ge.atm.atmservice.domain.dao.Card;
import ge.atm.atmservice.domain.dto.AuthenticationMethod;
import ge.atm.atmservice.service.CardService;
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
        final Card card = cardService.getCard(cardNumber);
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(card.getCardNumber(), null);
        authenticationToken.setDetails(AuthenticationMethod.valueOf(card.getPreferredAuth().getId()));
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
