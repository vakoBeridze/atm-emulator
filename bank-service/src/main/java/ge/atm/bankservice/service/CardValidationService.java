package ge.atm.bankservice.service;

public interface CardValidationService {

    String PIN_CODE_VALIDATOR = "pin-code-validator";
    String FINGERPRINT_VALIDATOR = "fingerprint-validator";

    /**
     * Validates card depending on the given card number and secret
     *
     * @param cardNumber number of card
     * @param secret     secret for validation
     */
    void validate(String cardNumber, String secret);
}
