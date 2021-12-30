package ge.atm.bankservice.service.impl;

import ge.atm.bankservice.domain.dao.AuthMethod;
import ge.atm.bankservice.domain.dao.Card;
import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardDto;
import ge.atm.bankservice.domain.dto.CardValidationResult;
import ge.atm.bankservice.repository.CardRepository;
import ge.atm.bankservice.service.BankAccountService;
import ge.atm.bankservice.service.CardValidationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Slf4j
@Service
public class BankAccountServiceImpl implements BankAccountService {

    public static final int MAX_INCORRECT_ATTEMPTS = 3;
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
        final Card card = fetchCard(cardNumber);
        return modelMapper.map(card, CardDto.class);
    }

    private Card fetchCard(String cardNumber) {
        final Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card could not be found"));

        if (card.isBlocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card is blocked");
        }
        return card;
    }

    @Override
    public void validateCardSecret(String cardNumber, String secret, AuthenticationMethod preferredAuthenticationMethod) {
        log.debug("Validate card secret for card: {}, authMethod: {}", cardNumber, preferredAuthenticationMethod.name());
        CardValidationResult validationResult;
        final Card card = fetchCard(cardNumber);

        int incorrectAttempts = card.getIncorrectAttempts();

        switch (preferredAuthenticationMethod) {
            case PIN:
                validationResult = pinCodeValidator.validate(cardNumber, secret);
                break;
            case FINGERPRINT:
                validationResult = fingerprintValidator.validate(cardNumber, secret);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown authentication method");
        }

        if (validationResult.hasError()) {
            updateIncorrectAttempts(card, incorrectAttempts + 1);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, validationResult.getErrorMessage());
        }

        updateIncorrectAttempts(card, 0);
    }

    private void updateIncorrectAttempts(Card card, int incorrectAttempts) {
        card.setIncorrectAttempts(incorrectAttempts);
        card.setBlocked(incorrectAttempts >= MAX_INCORRECT_ATTEMPTS);
        cardRepository.save(card);
    }

    @Override
    public void updatePreferredAuth(String cardNumber, AuthenticationMethod preferredAuth) {
        log.debug("Update preferred authentication method for card: {}, authMethod: {}", cardNumber, preferredAuth.name());
        final Card card = fetchCard(cardNumber);
        card.setPreferredAuth(new AuthMethod(preferredAuth.getDatabaseId()));
        cardRepository.save(card);
    }

    @Override
    public BigDecimal getCurrentBalance(String cardNumber) {
        log.debug("Get current balance for card: {}", cardNumber);
        return fetchCard(cardNumber).getBalance();
    }

    @Override
    public BigDecimal depositCardBalance(String cardNumber, BigDecimal amount) {
        log.debug("Deposit money for card: {}, amount: {}", cardNumber, amount);
        final Card card = fetchCard(cardNumber);

        final BigDecimal currentBalance = card.getBalance();
        card.setBalance(currentBalance.add(amount));

        try {
            final Card savedCard = cardRepository.save(card);
            return savedCard.getBalance();
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("Error while depositing money for card: {}, amount: {}", cardNumber, amount, e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not complete transaction.");
        }
    }

    @Override
    public BigDecimal withdrawCardBalance(String cardNumber, BigDecimal amount) {
        log.debug("Withdraw money for card: {}, amount: {}", cardNumber, amount);
        final Card card = fetchCard(cardNumber);

        final BigDecimal currentBalance = card.getBalance();
        if (currentBalance.compareTo(amount) < 0) {
            log.warn("Balance: {} for card: {} is less then requested amount: {} ", currentBalance, cardNumber, amount);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance is less then requested amount.");
        }

        card.setBalance(currentBalance.subtract(amount));

        try {
            final Card savedCard = cardRepository.save(card);
            return savedCard.getBalance();
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("Error while withdrawing money for card: {}, amount: {}", cardNumber, amount, e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not complete transaction.");
        }
    }
}
