package ge.atm.bankservice.service.impl;

import ge.atm.bankservice.domain.dao.Card;
import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardDto;
import ge.atm.bankservice.domain.dto.CardValidationResult;
import ge.atm.bankservice.repository.CardRepository;
import ge.atm.bankservice.service.BankAccountService;
import ge.atm.bankservice.service.CardValidationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BankAccountServiceImplTest {

    private CardRepository cardRepository;
    private CardValidationService pinCodeValidator;
    private CardValidationService fingerprintValidator;
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        pinCodeValidator = mock(CardValidationService.class);
        fingerprintValidator = mock(CardValidationService.class);
        bankAccountService = new BankAccountServiceImpl(cardRepository, new ModelMapper(), pinCodeValidator, fingerprintValidator);
    }

    @Test
    void getCard() {
        // Given
        final Card mockCard = Card.builder().balance(BigDecimal.valueOf(999)).build();
        doReturn(Optional.of(mockCard)).when(cardRepository).findByCardNumber(any());

        // When
        final CardDto result = bankAccountService.getCard("1111");

        // Then
        verify(cardRepository, times(1)).findByCardNumber(any());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BigDecimal.valueOf(999), result.getBalance());
    }

    @Test
    void validateCardSecretFingerprint() {
        // Given
        final Card mockCard = Card.builder().balance(BigDecimal.valueOf(999)).build();
        doReturn(Optional.of(mockCard)).when(cardRepository).findByCardNumber(any());
        doReturn(new CardValidationResult()).when(fingerprintValidator).validate(any(), any());

        // When
        bankAccountService.validateCardSecret("1111", "1234", AuthenticationMethod.FINGERPRINT);

        // Then
        verify(fingerprintValidator, times(1)).validate(any(), any());
        verify(pinCodeValidator, times(0)).validate(any(), any());
    }

    @Test
    void validateCardSecretPin() {
        // Given
        final Card mockCard = Card.builder().balance(BigDecimal.valueOf(999)).build();
        doReturn(Optional.of(mockCard)).when(cardRepository).findByCardNumber(any());
        doReturn(new CardValidationResult()).when(pinCodeValidator).validate(any(), any());

        // When
        bankAccountService.validateCardSecret("1111", "1234", AuthenticationMethod.PIN);

        // Then
        verify(fingerprintValidator, times(0)).validate(any(), any());
        verify(pinCodeValidator, times(1)).validate(any(), any());
    }

    @Test
    void validateCardSecretPinError() {
        // Given
        final Card mockCard = Card.builder().balance(BigDecimal.valueOf(999)).build();
        doReturn(Optional.of(mockCard)).when(cardRepository).findByCardNumber(any());
        doReturn(mockCard).when(cardRepository).save(any());
        doReturn(new CardValidationResult("invalid pin code")).when(pinCodeValidator).validate(any(), any());

        // When
        // Then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> bankAccountService.validateCardSecret("1111", "1234", AuthenticationMethod.PIN));
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("invalid pin code"));
        verify(cardRepository, times(1)).findByCardNumber(any());
        verify(cardRepository, times(1)).save(any());
        verify(fingerprintValidator, times(0)).validate(any(), any());
        verify(pinCodeValidator, times(1)).validate(any(), any());
    }

    @Test
    void updatePreferredAuth() {
        // Given
        final Card mockCard = Card.builder().balance(BigDecimal.valueOf(999)).build();
        doReturn(Optional.of(mockCard)).when(cardRepository).findByCardNumber(any());
        doReturn(mockCard).when(cardRepository).save(any());

        // When
        bankAccountService.updatePreferredAuth("1111", AuthenticationMethod.PIN);

        // Then
        verify(cardRepository, times(1)).findByCardNumber(any());
        verify(cardRepository, times(1)).save(any());
    }

    @Test
    void getCurrentBalance() {
        // Given
        final Card mockCard = Card.builder().balance(BigDecimal.valueOf(999)).build();
        doReturn(Optional.of(mockCard)).when(cardRepository).findByCardNumber(any());

        // When
        final BigDecimal result = bankAccountService.getCurrentBalance("1111");

        // Then
        verify(cardRepository, times(1)).findByCardNumber(any());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BigDecimal.valueOf(999), result);
    }

    @Test
    void depositCardBalance() {
        // Given
        final Card mockCard = Card.builder().balance(BigDecimal.valueOf(800)).build();
        final Card mockSavedCard = Card.builder().balance(BigDecimal.valueOf(900)).build();
        doReturn(Optional.of(mockCard)).when(cardRepository).findByCardNumber(any());
        doReturn(mockSavedCard).when(cardRepository).save(any());

        // When
        final BigDecimal balance = bankAccountService.depositCardBalance("1111", BigDecimal.valueOf(100));

        // Then
        Assertions.assertEquals(BigDecimal.valueOf(900), balance);
        verify(cardRepository, times(1)).findByCardNumber(any());
        verify(cardRepository, times(1)).save(any());
    }

    @Test
    void withdrawCardBalanceNotEnoughBalance() {
        // Given
        final Card mockCard = Card.builder().balance(BigDecimal.valueOf(999)).build();
        doReturn(Optional.of(mockCard)).when(cardRepository).findByCardNumber(any());

        // When
        // Then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> bankAccountService.withdrawCardBalance("1111", BigDecimal.valueOf(10000)));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("Balance is less then requested amount"));
        verify(cardRepository, times(1)).findByCardNumber(any());
    }

    @Test
    void withdrawCardBalance() {
        // Given
        final Card mockCard = Card.builder().balance(BigDecimal.valueOf(800)).build();
        final Card mockSavedCard = Card.builder().balance(BigDecimal.valueOf(700)).build();
        doReturn(Optional.of(mockCard)).when(cardRepository).findByCardNumber(any());
        doReturn(mockSavedCard).when(cardRepository).save(any());

        // When
        final BigDecimal balance = bankAccountService.withdrawCardBalance("1111", BigDecimal.valueOf(100));

        // Then
        Assertions.assertEquals(BigDecimal.valueOf(700), balance);
        verify(cardRepository, times(1)).findByCardNumber(any());
        verify(cardRepository, times(1)).save(any());
    }
}