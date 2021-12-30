package ge.atm.atmservice.service;

import ge.atm.bankservice.domain.dto.CardDto;

public interface CardService {

    /**
     * Gets card details by given card number
     *
     * @param cardNumber card number
     * @return Card details
     */
    CardDto getCard(String cardNumber);

    /**
     * Sets preferred authentication method for authenticated card
     *
     * @param preferredAuth method to set
     */
    void updatePreferredAuth(CardDto.PreferredAuthEnum preferredAuth);
}
