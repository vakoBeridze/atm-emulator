package ge.atm.atmservice.service;

import ge.atm.atmservice.domain.AuthenticationRequest;
import ge.atm.atmservice.domain.AuthenticationResponse;

public interface AuthenticationService {

    /**
     * Initiate authentication with card number
     *
     * @param request authentication request
     * @return authentication response
     */
    AuthenticationResponse initiateAuthentication(AuthenticationRequest request);

    /**
     * Finalise authentication using preferred authentication method
     *
     * @param request authentication request
     * @return authentication response
     */
    AuthenticationResponse finalizeAuthentication(AuthenticationRequest request);
}
