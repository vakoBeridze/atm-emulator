package ge.atm.bankservice.api;

import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardDto;
import ge.atm.bankservice.service.CardService;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/card")
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<CardDto> getCard(@ApiParam(name = "cardNumber", required = true) @RequestParam String cardNumber) {
        final CardDto card = cardService.getCard(cardNumber);
        return ResponseEntity.ok(card);
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validateCardSecret(@ApiParam(name = "cardNumber", required = true) @RequestParam String cardNumber,
                                                   @ApiParam(name = "secret", required = true) @RequestParam String secret,
                                                   @ApiParam(name = "preferredAuth", required = true) @RequestParam AuthenticationMethod preferredAuth) {
        cardService.validateSecret(cardNumber, secret, preferredAuth);
        return ResponseEntity.ok().build();
    }

}
