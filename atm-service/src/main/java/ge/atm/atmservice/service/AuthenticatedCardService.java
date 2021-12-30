package ge.atm.atmservice.service;

import ge.atm.atmservice.security.jwt.JwtCardDetails;

public interface AuthenticatedCardService {
    /**
     * Gets authenticated card details from security context
     *
     * @return JwtCardDetails
     */
    JwtCardDetails getAuthenticatedCard();
}
