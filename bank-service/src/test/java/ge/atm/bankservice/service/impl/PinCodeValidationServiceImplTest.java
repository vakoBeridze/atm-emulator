package ge.atm.bankservice.service.impl;

import ge.atm.bankservice.domain.dao.CardCredential;
import ge.atm.bankservice.repository.CardCredentialRepository;
import ge.atm.bankservice.service.CardValidationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PinCodeValidationServiceImplTest {

    private CardCredentialRepository cardCredentialRepository;
    private CardValidationService cardValidationService;

    @BeforeEach
    void setUp() {
        cardCredentialRepository = mock(CardCredentialRepository.class);
        cardValidationService = new PinCodeValidationServiceImpl(cardCredentialRepository);
    }

    @Test
    void validate() {

        // Given
        final CardCredential mockCardCredential = CardCredential.builder().secret("12345678").build();
        doReturn(Optional.of(mockCardCredential)).when(cardCredentialRepository).findByCard_CardNumberAndCredentialType_Id(any(), any());

        // When
        cardValidationService.validate("1111", "12345678");

        // Then
        verify(cardCredentialRepository, times(1)).findByCard_CardNumberAndCredentialType_Id(any(), any());
    }

    @Test
    void validateCredentialNotSet() {
        // Given
        doReturn(Optional.empty()).when(cardCredentialRepository).findByCard_CardNumberAndCredentialType_Id(any(), any());

        // When
        // Then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> cardValidationService.validate("1111", "1234"));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("Authentication method PIN code not set"));
        verify(cardCredentialRepository, times(1)).findByCard_CardNumberAndCredentialType_Id(any(), any());
    }

    @Test
    void validateCredentialInvalid() {
        // Given
        final CardCredential mockCardCredential = CardCredential.builder().secret("12345678").build();
        doReturn(Optional.of(mockCardCredential)).when(cardCredentialRepository).findByCard_CardNumberAndCredentialType_Id(any(), any());

        // When
        // Then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> cardValidationService.validate("1111", "1234"));
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("Incorrect pin code"));
        verify(cardCredentialRepository, times(1)).findByCard_CardNumberAndCredentialType_Id(any(), any());
    }
}
