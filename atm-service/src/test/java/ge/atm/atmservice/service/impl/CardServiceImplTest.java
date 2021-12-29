package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.service.CardService;
import ge.atm.bankservice.api.BankAccountControllerApi;
import ge.atm.bankservice.domain.dto.CardDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CardServiceImplTest {

    private BankAccountControllerApi bankAccountControllerApi;
    private CardService cardService;


    @BeforeEach
    void init() {
        bankAccountControllerApi = mock(BankAccountControllerApi.class);
        cardService = new CardServiceImpl(bankAccountControllerApi);
    }

    @Test
    void getCard() {
        // Given
        final CardDto mockCard = new CardDto();
        mockCard.setCardNumber("zzz");
        doReturn(mockCard).when(bankAccountControllerApi).getCardUsingGET(any());

        // When
        final CardDto result = cardService.getCard("123");

        // Then
        verify(bankAccountControllerApi, times(1)).getCardUsingGET(any());
        Assertions.assertNotNull(result);
        Assertions.assertEquals("zzz", result.getCardNumber());
    }
}