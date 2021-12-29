package ge.atm.atmservice.service;

import ge.atm.atmservice.domain.AuthenticationRequest;
import ge.atm.atmservice.domain.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse initiateAuthentication(AuthenticationRequest request);

    AuthenticationResponse finalizeAuthentication(AuthenticationRequest request);
}
