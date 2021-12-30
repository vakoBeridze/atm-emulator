package ge.atm.bankservice.service.impl;

import ge.atm.bankservice.domain.dao.CardCredential;
import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardValidationResult;
import ge.atm.bankservice.repository.CardCredentialRepository;
import ge.atm.bankservice.service.CardValidationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
@Qualifier(CardValidationService.PIN_CODE_VALIDATOR)
public class PinCodeValidationServiceImpl implements CardValidationService {

    private final CardCredentialRepository cardCredentialRepository;

    @Override
    public CardValidationResult validate(String cardNumber, String secret) {
        final CardCredential credential = cardCredentialRepository.findByCard_CardNumberAndCredentialType_Id(cardNumber, AuthenticationMethod.PIN.getDatabaseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authentication method PIN code not set."));

        final CardValidationResult validationResult = new CardValidationResult();
        if (!StringUtils.hasText(secret) || !secret.equals(credential.getSecret())) {
            validationResult.setErrorMessage("Incorrect pin code.");
        }
        return validationResult;
    }
}
