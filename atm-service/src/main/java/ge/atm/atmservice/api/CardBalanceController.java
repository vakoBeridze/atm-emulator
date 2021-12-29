package ge.atm.atmservice.api;

import ge.atm.atmservice.service.CardBalanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Api("Balance API")
@RequestMapping(path = "/api/balance")
public class CardBalanceController {

    private final CardBalanceService cardBalanceService;

    public CardBalanceController(CardBalanceService cardBalanceService) {
        this.cardBalanceService = cardBalanceService;
    }

    @ApiOperation(value = "Get current balance for authenticated card")
    @GetMapping
    public BigDecimal getCurrentBalance() {
        return cardBalanceService.getCurrentBalance();
    }

    @ApiOperation(value = "Deposit money for authenticated card")
    @PostMapping("/deposit")
    public ResponseEntity<BigDecimal> depositCardBalance(@ApiParam(name = "amount", required = true, example = "42.7") @RequestParam BigDecimal amount) {
        final BigDecimal balance = cardBalanceService.depositCardBalance(amount);
        return ResponseEntity.ok(balance);
    }

    @ApiOperation(value = "Withdraw money for authenticated card")
    @PostMapping("/withdraw")
    public ResponseEntity<BigDecimal> withdrawCardBalance(@ApiParam(name = "amount", required = true, example = "17.02") @RequestParam BigDecimal amount) {
        final BigDecimal balance = cardBalanceService.withdrawCardBalance(amount);
        return ResponseEntity.ok(balance);
    }
}
