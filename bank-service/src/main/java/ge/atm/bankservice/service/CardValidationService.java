package ge.atm.bankservice.service;

import ge.atm.bankservice.domain.dto.CardValidationResult;

public interface CardValidationService {

    String PIN_CODE_VALIDATOR = "pin-code-validator";
    String FINGERPRINT_VALIDATOR = "fingerprint-validator";

    /**
     * Validates card depending on the given card number and secret
     *
     * @param cardNumber number of card
     * @param secret     secret for validation
     * @return ValidationResult
     */
    CardValidationResult validate(String cardNumber, String secret);
}
