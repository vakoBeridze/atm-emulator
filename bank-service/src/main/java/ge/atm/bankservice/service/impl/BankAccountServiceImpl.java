package ge.atm.bankservice.service.impl;

import ge.atm.bankservice.domain.dao.Card;
import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardDto;
import ge.atm.bankservice.repository.CardRepository;
import ge.atm.bankservice.service.BankAccountService;
import ge.atm.bankservice.service.CardValidationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Slf4j
@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;
    private final CardValidationService pinCodeValidator;
    private final CardValidationService fingerprintValidator;

    public BankAccountServiceImpl(CardRepository cardRepository, ModelMapper modelMapper,
                                  @Qualifier(CardValidationService.PIN_CODE_VALIDATOR) CardValidationService pinCodeValidator,
                                  @Qualifier(CardValidationService.FINGERPRINT_VALIDATOR) CardValidationService fingerprintValidator) {
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
        this.pinCodeValidator = pinCodeValidator;
        this.fingerprintValidator = fingerprintValidator;
    }

    public CardDto getCard(String cardNumber) {
        log.debug("Get card details for card: {}", cardNumber);
        return cardRepository.findByCardNumber(cardNumber)
                .map(card -> modelMapper.map(card, CardDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card could not be found"));
    }

    @Override
    public void validateCardSecret(String cardNumber, String secret, AuthenticationMethod preferredAuthenticationMethod) {
        log.debug("Validate card secret for card: {}, authMethod: {}", cardNumber, preferredAuthenticationMethod.name());
        switch (preferredAuthenticationMethod) {
            case PIN:
                pinCodeValidator.validate(cardNumber, secret);
                break;
            case FINGERPRINT:
                fingerprintValidator.validate(cardNumber, secret);
                break;
        }
    }

    @Override
    public BigDecimal getCurrentBalance(String cardNumber) {
        log.debug("Get current balance for card: {}", cardNumber);
        return cardRepository.findByCardNumber(cardNumber)
                .map(Card::getBalance)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card could not be found"));
    }

    @Override
    public BigDecimal depositCardBalance(String cardNumber, BigDecimal amount) {
        log.debug("Deposit money for card: {}, amount: {}", cardNumber, amount);
        // TODO
        return null;
    }

    @Override
    public BigDecimal withdrawCardBalance(String cardNumber, BigDecimal amount) {
        log.debug("Withdraw money for card: {}, amount: {}", cardNumber, amount);
        // TODO
        return null;
    }
}
