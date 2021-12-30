package ge.atm.bankservice.service.impl;

import ge.atm.bankservice.domain.dao.Card;
import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardDto;
import ge.atm.bankservice.repository.CardRepository;
import ge.atm.bankservice.service.BankAccountService;
import ge.atm.bankservice.service.CardValidationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

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

        // When
        bankAccountService.validateCardSecret("1111", "1234", AuthenticationMethod.PIN);

        // Then
        verify(fingerprintValidator, times(0)).validate(any(), any());
        verify(pinCodeValidator, times(1)).validate(any(), any());
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
        // TODO
        // Given

        // When
        final BigDecimal result = bankAccountService.depositCardBalance("1111", BigDecimal.TEN);

        // Then
    }

    @Test
    void withdrawCardBalance() {
        // TODO
        // Given

        // When
        final BigDecimal result = bankAccountService.withdrawCardBalance("1111", BigDecimal.ONE);

        // Then
    }
}