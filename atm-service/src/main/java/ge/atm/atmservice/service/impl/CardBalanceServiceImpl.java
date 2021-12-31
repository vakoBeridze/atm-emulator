package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.service.AuthenticatedCardService;
import ge.atm.atmservice.service.CardBalanceService;
import ge.atm.bankservice.api.BankAccountControllerApi;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static ge.atm.atmservice.config.Resilience4jConfig.BANK_SERVICE;

@Slf4j
@AllArgsConstructor
@Service
public class CardBalanceServiceImpl implements CardBalanceService {

    private final AuthenticatedCardService authenticatedCardService;
    private final BankAccountControllerApi bankAccountControllerApi;

    @CircuitBreaker(name = BANK_SERVICE)
    @Override
    public BigDecimal getCurrentBalance() {
        final String cardNumber = authenticatedCardService.getAuthenticatedCard().getCardNumber();
        try {
            log.debug("Accessing bank account api to get current balance. card number: {}", cardNumber);
            return bankAccountControllerApi.getCardBalanceUsingGET(cardNumber);
        } catch (HttpClientErrorException e) {
            log.error("Error while accessing from external bank api to get current balance.", e);
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }

    @CircuitBreaker(name = BANK_SERVICE)
    @Override
    public BigDecimal depositCardBalance(BigDecimal amount) {
        final String cardNumber = authenticatedCardService.getAuthenticatedCard().getCardNumber();
        try {
            log.debug("Accessing bank account api to deposit balance. card number: {}", cardNumber);
            return bankAccountControllerApi.depositCardBalanceUsingPUT(amount, cardNumber);
        } catch (HttpClientErrorException e) {
            log.error("Error while accessing from external bank api to deposit balance.", e);
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }

    @CircuitBreaker(name = BANK_SERVICE)
    @Override
    public BigDecimal withdrawCardBalance(BigDecimal amount) {
        final String cardNumber = authenticatedCardService.getAuthenticatedCard().getCardNumber();
        try {
            log.debug("Accessing bank account api to withdraw balance. card number: {}", cardNumber);
            return bankAccountControllerApi.withdrawCardBalanceUsingPUT(amount, cardNumber);
        } catch (HttpClientErrorException e) {
            log.error("Error while accessing from external bank api to withdraw balance.", e);
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }
}
