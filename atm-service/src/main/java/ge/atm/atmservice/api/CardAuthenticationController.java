package ge.atm.atmservice.api;

import ge.atm.atmservice.service.CardService;
import ge.atm.bankservice.domain.dto.CardDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Api("Card API")
@RequestMapping(path = "/api/card")
public class CardAuthenticationController {

    private final CardService cardService;

    @ApiOperation(value = "Update preferred authentication method for authenticated card")
    @PutMapping("/preferred-auth")
    public ResponseEntity<Void> updatePreferredAuth(@ApiParam(name = "preferredAuth", required = true) @RequestParam CardDto.PreferredAuthEnum preferredAuth) {
        cardService.updatePreferredAuth(preferredAuth);
        return ResponseEntity.ok().build();
    }
}
