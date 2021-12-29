package ge.atm.atmservice.service;

import java.math.BigDecimal;

public interface CardBalanceService {

    BigDecimal getCurrentBalance();

    BigDecimal depositCardBalance(BigDecimal amount);

    BigDecimal withdrawCardBalance(BigDecimal amount);
}
