package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.security.jwt.JwtCardDetails;
import ge.atm.atmservice.service.AuthenticatedCardService;
import ge.atm.atmservice.service.CardBalanceService;
import ge.atm.bankservice.api.BankAccountControllerApi;
import ge.atm.bankservice.domain.dto.CardDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CardBalanceServiceImplTest {

    private AuthenticatedCardService authenticatedCardService;
    private BankAccountControllerApi bankAccountControllerApi;
    private CardBalanceService cardBalanceService;

    @BeforeEach
    void init() {
        authenticatedCardService = mock(AuthenticatedCardService.class);
        bankAccountControllerApi = mock(BankAccountControllerApi.class);
        cardBalanceService = new CardBalanceServiceImpl(authenticatedCardService, bankAccountControllerApi);
    }


    @Test
    void getCurrentBalance() {
        // Given
        doReturn(new JwtCardDetails("1234", CardDto.PreferredAuthEnum.PIN, null)).when(authenticatedCardService).getAuthenticatedCard();
        doReturn(BigDecimal.TEN).when(bankAccountControllerApi).getCardBalanceUsingGET(any());

        // When
        final BigDecimal result = cardBalanceService.getCurrentBalance();

        // Then
        verify(authenticatedCardService, times(1)).getAuthenticatedCard();
        verify(bankAccountControllerApi, times(1)).getCardBalanceUsingGET(any());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BigDecimal.TEN, result);
    }

    @Test
    void depositCardBalance() {
        // Given
        doReturn(new JwtCardDetails("1234", CardDto.PreferredAuthEnum.PIN, null)).when(authenticatedCardService).getAuthenticatedCard();
        doReturn(BigDecimal.TEN).when(bankAccountControllerApi).depositCardBalanceUsingPUT(any(), any());

        // When
        final BigDecimal result = cardBalanceService.depositCardBalance(BigDecimal.ONE);

        // Then
        verify(authenticatedCardService, times(1)).getAuthenticatedCard();
        verify(bankAccountControllerApi, times(1)).depositCardBalanceUsingPUT(any(), any());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BigDecimal.TEN, result);
    }

    @Test
    void withdrawCardBalance() {
        // Given
        doReturn(new JwtCardDetails("1234", CardDto.PreferredAuthEnum.PIN, null)).when(authenticatedCardService).getAuthenticatedCard();
        doReturn(BigDecimal.TEN).when(bankAccountControllerApi).withdrawCardBalanceUsingPUT(any(), any());

        // When
        final BigDecimal result = cardBalanceService.withdrawCardBalance(BigDecimal.ONE);

        // Then
        verify(authenticatedCardService, times(1)).getAuthenticatedCard();
        verify(bankAccountControllerApi, times(1)).withdrawCardBalanceUsingPUT(any(), any());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BigDecimal.TEN, result);
    }
}