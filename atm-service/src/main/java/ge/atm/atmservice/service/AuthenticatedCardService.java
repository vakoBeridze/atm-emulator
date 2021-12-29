package ge.atm.atmservice.service;

import ge.atm.atmservice.security.jwt.JwtCardDetails;

public interface AuthenticatedCardService {
    JwtCardDetails getAuthenticatedCard();
}
