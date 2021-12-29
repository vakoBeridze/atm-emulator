package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.service.CardService;
import ge.atm.bankservice.api.CardControllerApi;
import ge.atm.bankservice.domain.dto.CardDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final CardControllerApi cardControllerApi;

    public CardDto getCard(String cardNumber) {
        return cardControllerApi.getCardUsingGET(cardNumber);
    }
}
