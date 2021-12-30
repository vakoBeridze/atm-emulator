package ge.atm.atmservice.service;

import java.math.BigDecimal;

public interface CardBalanceService {

    /**
     * Get current balance for authenticated card
     *
     * @return balance
     */
    BigDecimal getCurrentBalance();

    /**
     * Deposit money for authenticated card
     *
     * @param amount amount to add to existing balance
     * @return final balance
     */
    BigDecimal depositCardBalance(BigDecimal amount);

    /**
     * Withdraw money for authenticated card
     *
     * @param amount amount to subtract to existing balance
     * @return final balance
     */
    BigDecimal withdrawCardBalance(BigDecimal amount);
}
