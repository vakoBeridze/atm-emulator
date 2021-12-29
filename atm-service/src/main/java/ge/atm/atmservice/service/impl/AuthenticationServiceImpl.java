package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.domain.dto.AuthenticationRequest;
import ge.atm.atmservice.domain.dto.AuthenticationResponse;
import ge.atm.atmservice.security.jwt.JwtCardDetails;
import ge.atm.atmservice.security.jwt.JwtTokenProvider;
import ge.atm.atmservice.service.AuthenticatedCardService;
import ge.atm.atmservice.service.AuthenticationService;
import ge.atm.bankservice.api.BankAccountControllerApi;
import ge.atm.bankservice.domain.dto.CardDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticatedCardService authenticatedCardService;
    private final BankAccountControllerApi bankAccountControllerApi;

    public AuthenticationResponse initiateAuthentication(AuthenticationRequest request) {
        log.debug("Initiate authentication with card number: {}", request.getCardNumber());
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCardNumber(), null));
        final CardDto.PreferredAuthEnum preferredAuth = (CardDto.PreferredAuthEnum) authentication.getDetails();
        final String accessToken = tokenProvider.generateToken(authentication.getName(), preferredAuth, false);
        return buildResponse(accessToken, request.getCardNumber(), preferredAuth);
    }

    public AuthenticationResponse finalizeAuthentication(AuthenticationRequest request) {
        log.debug("Finalise authentication for card number: {}", request.getCardNumber());
        final JwtCardDetails authenticatedCard = authenticatedCardService.getAuthenticatedCard();
        final String authenticatedCardNumber = authenticatedCard.getCardNumber();
        final CardDto.PreferredAuthEnum preferredAuthenticationMethod = authenticatedCard.getPreferredAuthenticationMethod();
        final String requestedCardNumber = request.getCardNumber();
        final String secret = request.getSecret();

        if (!requestedCardNumber.equals(authenticatedCardNumber)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Requested card is not permitted to finalize authentication.");
        }

        log.debug("Accessing bank account api for secret validation. card number: {}", request.getCardNumber());
        bankAccountControllerApi.validateCardSecretUsingPOST(authenticatedCardNumber, preferredAuthenticationMethod.name(), secret);

        final String accessToken = tokenProvider.generateToken(authenticatedCard.getUsername(), preferredAuthenticationMethod, true);
        return buildResponse(accessToken, requestedCardNumber, preferredAuthenticationMethod);
    }

    private AuthenticationResponse buildResponse(String accessToken, String cardNumber, CardDto.PreferredAuthEnum preferredAuthenticationMethod) {
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .cardNumber(cardNumber)
                .preferredAuthentication(preferredAuthenticationMethod)
                .build();
    }
}
