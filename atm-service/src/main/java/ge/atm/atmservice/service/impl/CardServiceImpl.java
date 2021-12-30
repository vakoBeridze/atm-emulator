package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.service.AuthenticatedCardService;
import ge.atm.atmservice.service.CardService;
import ge.atm.bankservice.api.BankAccountControllerApi;
import ge.atm.bankservice.domain.dto.CardDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@AllArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final AuthenticatedCardService authenticatedCardService;
    private final BankAccountControllerApi bankAccountControllerApi;

    public CardDto getCard(String cardNumber) {
        try {
            log.debug("Accessing bank account api to get card details. card number: {}", cardNumber);
            return bankAccountControllerApi.getCardUsingGET(cardNumber);
        } catch (HttpClientErrorException e) {
            log.error("Error while accessing from external bank api to get card details.", e);
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }

    @Override
    public void updatePreferredAuth(CardDto.PreferredAuthEnum preferredAuth) {
        final String cardNumber = authenticatedCardService.getAuthenticatedCard().getCardNumber();
        try {
            log.debug("Accessing bank account api to update preferred auth method. card number: {}", cardNumber);
            bankAccountControllerApi.updatePreferredAuthUsingPUT(cardNumber, preferredAuth.name());
        } catch (HttpClientErrorException e) {
            log.error("Error while accessing from external bank api to update preferred auth method.", e);
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }
}
