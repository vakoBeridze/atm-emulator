package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.domain.AuthenticationRequest;
import ge.atm.atmservice.domain.AuthenticationResponse;
import ge.atm.atmservice.security.jwt.JwtCardDetails;
import ge.atm.atmservice.security.jwt.JwtTokenProvider;
import ge.atm.atmservice.service.AuthenticatedCardService;
import ge.atm.atmservice.service.AuthenticationService;
import ge.atm.bankservice.api.BankAccountControllerApi;
import ge.atm.bankservice.domain.dto.CardDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AuthenticationServiceImplTest {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private AuthenticatedCardService authenticatedCardService;
    private BankAccountControllerApi bankAccountControllerApi;
    private AuthenticationService authenticationService;

    @BeforeEach
    void init() {
        authenticationManager = mock(AuthenticationManager.class);
        tokenProvider = mock(JwtTokenProvider.class);
        authenticatedCardService = mock(AuthenticatedCardService.class);
        bankAccountControllerApi = mock(BankAccountControllerApi.class);
        authenticationService = new AuthenticationServiceImpl(authenticationManager, tokenProvider, authenticatedCardService, bankAccountControllerApi);
    }

    @Test
    void initiateAuthentication() {
        // Given
        doReturn(new UsernamePasswordAuthenticationToken("zzz", null)).when(authenticationManager).authenticate(any());
        doReturn("access-token").when(tokenProvider).generateToken(any(), any(), any());

        // When
        final AuthenticationResponse result = authenticationService.initiateAuthentication(AuthenticationRequest.builder().build());

        // Then
        verify(authenticationManager, times(1)).authenticate(any());
        verify(tokenProvider, times(1)).generateToken(any(), any(), any());
        Assertions.assertNotNull(result);
        Assertions.assertEquals("access-token", result.getAccessToken());
    }

    @Test
    void finalizeAuthenticationForbidden() {
        // Given
        doReturn(new JwtCardDetails("1234", CardDto.PreferredAuthEnum.PIN, null)).when(authenticatedCardService).getAuthenticatedCard();

        // When
        // Then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> authenticationService.finalizeAuthentication(AuthenticationRequest.builder().cardNumber("1111").build()));
        Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("Requested card is not permitted to finalize authentication"));
        verify(authenticatedCardService, times(1)).getAuthenticatedCard();
        verify(tokenProvider, times(0)).generateToken(any(), any(), any());

    }

    @Test
    void finalizeAuthentication() {
        // Given
        doReturn(new JwtCardDetails("1234", CardDto.PreferredAuthEnum.PIN, null)).when(authenticatedCardService).getAuthenticatedCard();
        doNothing().when(bankAccountControllerApi).validateCardSecretUsingPOST(any(), any(), any());
        doReturn("access-token").when(tokenProvider).generateToken(any(), any(), any());

        // When
        final AuthenticationResponse result = authenticationService.finalizeAuthentication(AuthenticationRequest.builder().cardNumber("1234").build());

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("access-token", result.getAccessToken());
        verify(authenticatedCardService, times(1)).getAuthenticatedCard();
        verify(bankAccountControllerApi, times(1)).validateCardSecretUsingPOST(any(), any(), any());
        verify(tokenProvider, times(1)).generateToken(any(), any(), any());

    }
}