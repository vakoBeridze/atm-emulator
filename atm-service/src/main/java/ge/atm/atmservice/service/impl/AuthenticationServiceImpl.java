package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.domain.dto.AuthenticationMethod;
import ge.atm.atmservice.domain.dto.AuthenticationRequest;
import ge.atm.atmservice.domain.dto.AuthenticationResponse;
import ge.atm.atmservice.security.jwt.JwtCardDetails;
import ge.atm.atmservice.security.jwt.JwtTokenProvider;
import ge.atm.atmservice.service.AuthenticationService;
import ge.atm.atmservice.service.CardValidationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CardValidationService pinCodeValidator;
    private final CardValidationService fingerprintValidator;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider,
                                     @Qualifier(CardValidationService.PIN_CODE_VALIDATOR) CardValidationService pinCodeValidator,
                                     @Qualifier(CardValidationService.FINGERPRINT_VALIDATOR) CardValidationService fingerPrintValidator) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.pinCodeValidator = pinCodeValidator;
        this.fingerprintValidator = fingerPrintValidator;
    }

    public AuthenticationResponse initiateAuthentication(AuthenticationRequest request) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCardNumber(), null));
        final AuthenticationMethod authenticationMethod = (AuthenticationMethod) authentication.getDetails();
        final String accessToken = tokenProvider.generateToken(authentication.getName(), authenticationMethod, false);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .cardNumber(request.getCardNumber())
                .build();
    }

    public AuthenticationResponse finalizeAuthentication(AuthenticationRequest request) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final JwtCardDetails cardDetails = (JwtCardDetails) authentication.getPrincipal();
        final String authenticatedCardNumber = cardDetails.getUsername();
        final AuthenticationMethod preferredAuthenticationMethod = (AuthenticationMethod) authentication.getDetails();
        final String requestedCardNumber = request.getCardNumber();
        final String requestedCode = request.getSecret();

        if (!requestedCardNumber.equals(authenticatedCardNumber)) {
            throw new IllegalStateException("Requested card is not permitted to finalize authentication.");
        }

        switch (preferredAuthenticationMethod) {
            case PIN:
                pinCodeValidator.validate(authenticatedCardNumber, requestedCode);
                break;
            case FINGERPRINT:
                fingerprintValidator.validate(authenticatedCardNumber, requestedCode);
                break;
        }

        final String accessToken = tokenProvider.generateToken(authentication.getName(), preferredAuthenticationMethod, true);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .cardNumber(request.getCardNumber())
                .build();
    }
}
