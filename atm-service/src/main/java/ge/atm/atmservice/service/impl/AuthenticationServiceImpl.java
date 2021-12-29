package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.domain.dto.AuthenticationRequest;
import ge.atm.atmservice.domain.dto.AuthenticationResponse;
import ge.atm.atmservice.security.jwt.JwtCardDetails;
import ge.atm.atmservice.security.jwt.JwtTokenProvider;
import ge.atm.atmservice.service.AuthenticationService;
import ge.atm.bankservice.api.CardControllerApi;
import ge.atm.bankservice.domain.dto.CardDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CardControllerApi cardControllerApi;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, CardControllerApi cardControllerApi) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.cardControllerApi = cardControllerApi;
    }

    public AuthenticationResponse initiateAuthentication(AuthenticationRequest request) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCardNumber(), null));
        final CardDto.PreferredAuthEnum preferredAuth = (CardDto.PreferredAuthEnum) authentication.getDetails();
        final String accessToken = tokenProvider.generateToken(authentication.getName(), preferredAuth, false);
        return buildResponse(accessToken, request.getCardNumber(), preferredAuth);
    }

    public AuthenticationResponse finalizeAuthentication(AuthenticationRequest request) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final JwtCardDetails cardDetails = (JwtCardDetails) authentication.getPrincipal();
        final String authenticatedCardNumber = cardDetails.getUsername();
        final CardDto.PreferredAuthEnum preferredAuthenticationMethod = (CardDto.PreferredAuthEnum) authentication.getDetails();
        final String requestedCardNumber = request.getCardNumber();
        final String secret = request.getSecret();

        if (!requestedCardNumber.equals(authenticatedCardNumber)) {
            throw new IllegalStateException("Requested card is not permitted to finalize authentication.");
        }

        cardControllerApi.validateCardSecretUsingPOST(authenticatedCardNumber, preferredAuthenticationMethod.name(), secret);

        final String accessToken = tokenProvider.generateToken(authentication.getName(), preferredAuthenticationMethod, true);
        return buildResponse(accessToken, request.getCardNumber(), preferredAuthenticationMethod);
    }

    private AuthenticationResponse buildResponse(String accessToken, String cardNumber, CardDto.PreferredAuthEnum preferredAuthenticationMethod) {
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .cardNumber(cardNumber)
                .preferredAuthentication(preferredAuthenticationMethod)
                .build();
    }
}
