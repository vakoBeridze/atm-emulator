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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@AllArgsConstructor
@Api("Bank Account API")
@RestController
@RequestMapping(path = "/api/card")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @ApiOperation(value = "Get card details for given card number")
    @GetMapping("/{cardNumber}")
    public ResponseEntity<CardDto> getCard(@ApiParam(name = "cardNumber", required = true) @PathVariable String cardNumber) {
        final CardDto card = bankAccountService.getCard(cardNumber);
        return ResponseEntity.ok(card);
    }

    @ApiOperation(value = "Validate card secret for given card number")
    @PostMapping("/{cardNumber}/validate")
    public ResponseEntity<Void> validateCardSecret(@ApiParam(name = "cardNumber", required = true, example = "12345678") @PathVariable String cardNumber,
                                                   @ApiParam(name = "secret", required = true, example = "1234") @RequestParam String secret,
                                                   @ApiParam(name = "preferredAuth", required = true) @RequestParam AuthenticationMethod preferredAuth) {
        bankAccountService.validateCardSecret(cardNumber, secret, preferredAuth);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update preferred authentication method for given card number")
    @PutMapping("/{cardNumber}/preferred-auth")
    public ResponseEntity<Void> updatePreferredAuth(@ApiParam(name = "cardNumber", required = true, example = "12345678") @PathVariable String cardNumber,
                                                    @ApiParam(name = "preferredAuth", required = true) @RequestParam AuthenticationMethod preferredAuth) {
        bankAccountService.updatePreferredAuth(cardNumber, preferredAuth);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get current balance for given card number")
    @GetMapping("/{cardNumber}/balance")
    public ResponseEntity<BigDecimal> getCardBalance(@ApiParam(name = "cardNumber", required = true, example = "12345678") @PathVariable String cardNumber) {
        final BigDecimal balance = bankAccountService.getCurrentBalance(cardNumber);
        return ResponseEntity.ok(balance);
    }

    @ApiOperation(value = "Deposit money for given card number")
    @PutMapping("/{cardNumber}/deposit")
    public ResponseEntity<BigDecimal> depositCardBalance(@ApiParam(name = "cardNumber", required = true, example = "12345678") @PathVariable String cardNumber,
                                                         @ApiParam(name = "amount", required = true, example = "22.12") @RequestParam BigDecimal amount) {
        final BigDecimal balance = bankAccountService.depositCardBalance(cardNumber, amount);
        return ResponseEntity.ok(balance);
    }

    @ApiOperation(value = "Withdraw money for given card number")
    @PutMapping("/{cardNumber}/withdraw")
    public ResponseEntity<BigDecimal> withdrawCardBalance(@ApiParam(name = "cardNumber", required = true, example = "12345678") @PathVariable String cardNumber,
                                                          @ApiParam(name = "amount", required = true, example = "11.2") @RequestParam BigDecimal amount) {
        final BigDecimal balance = bankAccountService.withdrawCardBalance(cardNumber, amount);
        return ResponseEntity.ok(balance);
    }
}
