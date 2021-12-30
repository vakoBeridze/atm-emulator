package ge.atm.bankservice.service.impl;

import ge.atm.bankservice.domain.dao.AuthMethod;
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
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card could not be found"));
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
    public void updatePreferredAuth(String cardNumber, AuthenticationMethod preferredAuth) {
        log.debug("Update preferred authentication method for card: {}, authMethod: {}", cardNumber, preferredAuth.name());
        final Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card could not be found"));
        card.setPreferredAuth(new AuthMethod(preferredAuth.getDatabaseId()));
        cardRepository.save(card);
    }

    @Override
    public BigDecimal getCurrentBalance(String cardNumber) {
        log.debug("Get current balance for card: {}", cardNumber);
        return cardRepository.findByCardNumber(cardNumber)
                .map(Card::getBalance)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card could not be found"));
    }

    @Override
    public BigDecimal depositCardBalance(String cardNumber, BigDecimal amount) {
        log.debug("Deposit money for card: {}, amount: {}", cardNumber, amount);
        final Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card could not be found"));

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
        final Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card could not be found"));

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
