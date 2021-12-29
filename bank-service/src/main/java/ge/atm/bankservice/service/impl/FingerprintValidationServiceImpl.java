package ge.atm.bankservice.service.impl;

import ge.atm.bankservice.domain.dao.CardCredential;
import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.repository.CardCredentialRepository;
import ge.atm.bankservice.service.CardValidationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
@Qualifier(CardValidationService.FINGERPRINT_VALIDATOR)
public class FingerprintValidationServiceImpl implements CardValidationService {

    private final CardCredentialRepository cardCredentialRepository;

    @Override
    public void validate(String cardNumber, String secret) {
        final CardCredential credential = cardCredentialRepository.findByCard_CardNumberAndCredentialType_Id(cardNumber, AuthenticationMethod.FINGERPRINT.getDatabaseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authentication method FINGERPRINT not set."));

        if (!secret.equals(credential.getSecret())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Fingerprint not recognized.");
        }
    }
}
