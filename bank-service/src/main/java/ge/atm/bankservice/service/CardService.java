package ge.atm.bankservice.service;

import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardDto;
import org.springframework.stereotype.Service;

@Service
public interface CardService {

    CardDto getCard(String cardNumber);

    void validateSecret(String cardNumber, String secret, AuthenticationMethod preferredAuthenticationMethod);
}
