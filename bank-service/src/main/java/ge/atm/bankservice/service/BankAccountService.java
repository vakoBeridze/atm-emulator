package ge.atm.bankservice.service;

import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardDto;

import java.math.BigDecimal;

public interface BankAccountService {

    /**
     * Gets card details by given card number
     *
     * @param cardNumber card number
     * @return Card details
     */
    CardDto getCard(String cardNumber);

    /**
     * Validates card depending on preferred authentication method
     *
     * @param cardNumber                    number of card
     * @param secret                        pin code, fingerprint, ...
     * @param preferredAuthenticationMethod preferred authentication method
     */
    void validateCardSecret(String cardNumber, String secret, AuthenticationMethod preferredAuthenticationMethod);

    /**
     * Sets preferred authentication method for given card
     *
     * @param cardNumber    number of card
     * @param preferredAuth auth method to set
     */
    void updatePreferredAuth(String cardNumber, AuthenticationMethod preferredAuth);


    /**
     * Get current balance for given card
     *
     * @param cardNumber number of card
     * @return balance
     */
    BigDecimal getCurrentBalance(String cardNumber);

    /**
     * Deposit money for given card
     *
     * @param cardNumber number of card
     * @param amount     amount to add to existing balance
     * @return final balance
     */
    BigDecimal depositCardBalance(String cardNumber, BigDecimal amount);

    /**
     * Withdraw money for given card
     *
     * @param cardNumber number of card
     * @param amount     amount to subtract to existing balance
     * @return final balance
     */
    BigDecimal withdrawCardBalance(String cardNumber, BigDecimal amount);
}
