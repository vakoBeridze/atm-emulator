package ge.atm.bankservice.api;

import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardDto;
import ge.atm.bankservice.service.BankAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@AllArgsConstructor
@Api("Bank Account API")
@RestController
@RequestMapping(path = "/api/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @ApiOperation(value = "Get card details for given card number")
    @GetMapping("/card")
    public ResponseEntity<CardDto> getCard(@ApiParam(name = "cardNumber", required = true) @RequestParam String cardNumber) {
        final CardDto card = bankAccountService.getCard(cardNumber);
        return ResponseEntity.ok(card);
    }

    @ApiOperation(value = "Validate card secret for given card number")
    @PostMapping("/card/validate")
    public ResponseEntity<Void> validateCardSecret(@ApiParam(name = "cardNumber", required = true) @RequestParam String cardNumber,
                                                   @ApiParam(name = "secret", required = true) @RequestParam String secret,
                                                   @ApiParam(name = "preferredAuth", required = true) @RequestParam AuthenticationMethod preferredAuth) {
        bankAccountService.validateCardSecret(cardNumber, secret, preferredAuth);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get current balance for given card number")
    @GetMapping("/card/balance")
    public ResponseEntity<BigDecimal> getCardBalance(@ApiParam(name = "cardNumber", required = true) @RequestParam String cardNumber) {
        final BigDecimal balance = bankAccountService.getCurrentBalance(cardNumber);
        return ResponseEntity.ok(balance);
    }

    @ApiOperation(value = "Deposit money for given card number")
    @PostMapping("/card/deposit")
    public ResponseEntity<BigDecimal> depositCardBalance(@ApiParam(name = "cardNumber", required = true) @RequestParam String cardNumber,
                                                         @ApiParam(name = "amount", required = true) @RequestParam BigDecimal amount) {
        final BigDecimal balance = bankAccountService.depositCardBalance(cardNumber, amount);
        return ResponseEntity.ok(balance);
    }

    @ApiOperation(value = "Withdraw money for given card number")
    @PostMapping("/card/withdraw")
    public ResponseEntity<BigDecimal> withdrawCardBalance(@ApiParam(name = "cardNumber", required = true) @RequestParam String cardNumber,
                                                          @ApiParam(name = "amount", required = true) @RequestParam BigDecimal amount) {
        final BigDecimal balance = bankAccountService.withdrawCardBalance(cardNumber, amount);
        return ResponseEntity.ok(balance);
    }
}
