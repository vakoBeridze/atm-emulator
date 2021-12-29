package ge.atm.atmservice.service;

import ge.atm.atmservice.domain.dto.AuthenticationRequest;
import ge.atm.atmservice.domain.dto.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse initiateAuthentication(AuthenticationRequest request);

    AuthenticationResponse finalizeAuthentication(AuthenticationRequest request);
}
