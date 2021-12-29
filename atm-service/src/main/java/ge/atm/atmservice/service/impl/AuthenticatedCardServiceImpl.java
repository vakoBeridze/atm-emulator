package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.security.jwt.JwtCardDetails;
import ge.atm.atmservice.service.AuthenticatedCardService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedCardServiceImpl implements AuthenticatedCardService {

    @Override
    public JwtCardDetails getAuthenticatedCard() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtCardDetails) authentication.getPrincipal();
    }
}
