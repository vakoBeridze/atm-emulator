package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.service.AuthenticatedCardService;
import ge.atm.atmservice.service.CardBalanceService;
import ge.atm.bankservice.api.BankAccountControllerApi;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class CardBalanceServiceImpl implements CardBalanceService {

    private final AuthenticatedCardService authenticatedCardService;
    private final BankAccountControllerApi bankAccountControllerApi;

    @Override
    public BigDecimal getCurrentBalance() {
        final String cardNumber = authenticatedCardService.getAuthenticatedCard().getCardNumber();
        return bankAccountControllerApi.getCardBalanceUsingGET(cardNumber);
    }

    @Override
    public BigDecimal depositCardBalance(BigDecimal amount) {
        final String cardNumber = authenticatedCardService.getAuthenticatedCard().getCardNumber();
        return bankAccountControllerApi.depositCardBalanceUsingPUT(amount, cardNumber);
    }

    @Override
    public BigDecimal withdrawCardBalance(BigDecimal amount) {
        final String cardNumber = authenticatedCardService.getAuthenticatedCard().getCardNumber();
        return bankAccountControllerApi.withdrawCardBalanceUsingPUT(amount, cardNumber);
    }
}
