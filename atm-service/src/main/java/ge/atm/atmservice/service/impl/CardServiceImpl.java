package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.service.AuthenticatedCardService;
import ge.atm.atmservice.service.CardService;
import ge.atm.bankservice.api.BankAccountControllerApi;
import ge.atm.bankservice.domain.dto.CardDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final AuthenticatedCardService authenticatedCardService;
    private final BankAccountControllerApi bankAccountControllerApi;

    public CardDto getCard(String cardNumber) {
        return bankAccountControllerApi.getCardUsingGET(cardNumber);
    }

    @Override
    public void updatePreferredAuth(CardDto.PreferredAuthEnum preferredAuth) {
        final String cardNumber = authenticatedCardService.getAuthenticatedCard().getCardNumber();
        bankAccountControllerApi.updatePreferredAuthUsingPUT(cardNumber, preferredAuth.name());
    }
}
