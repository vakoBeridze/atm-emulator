package ge.atm.atmservice.service;

import ge.atm.bankservice.domain.dto.CardDto;

public interface CardService {

    CardDto getCard(String cardNumber);
}
