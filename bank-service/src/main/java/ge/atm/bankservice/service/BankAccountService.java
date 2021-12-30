package ge.atm.bankservice.service;

import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardDto;

import java.math.BigDecimal;

public interface BankAccountService {

    CardDto getCard(String cardNumber);

    void validateCardSecret(String cardNumber, String secret, AuthenticationMethod preferredAuthenticationMethod);

    void updatePreferredAuth(String cardNumber, AuthenticationMethod preferredAuth);

    BigDecimal getCurrentBalance(String cardNumber);

    BigDecimal depositCardBalance(String cardNumber, BigDecimal amount);

    BigDecimal withdrawCardBalance(String cardNumber, BigDecimal amount);
}
