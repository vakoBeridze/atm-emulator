package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.domain.dao.CardCredential;
import ge.atm.atmservice.domain.dto.AuthenticationMethod;
import ge.atm.atmservice.service.CardService;
import ge.atm.atmservice.service.CardValidationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@AllArgsConstructor
@Service
@Qualifier(CardValidationService.PIN_CODE_VALIDATOR)
public class PinCodeValidationServiceImpl implements CardValidationService {

    private final CardService cardService;

    @Override
    public void validate(String cardNumber, String secret) {
        final Set<CardCredential> credentials = cardService.getCard(cardNumber).getCredentials();
        final CardCredential credential = credentials.stream()
                .filter(cardCredential -> cardCredential.getCredentialType().getId() == AuthenticationMethod.PIN.getDatabaseId())
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authentication method PIN code not set."));


        if (!StringUtils.hasText(secret) || !secret.equals(credential.getSecret())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect pin code.");
        }
    }
}
