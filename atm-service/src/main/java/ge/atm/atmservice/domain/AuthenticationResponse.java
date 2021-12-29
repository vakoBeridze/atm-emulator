package ge.atm.atmservice.domain;

import ge.atm.bankservice.domain.dto.CardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String cardNumber;
    private CardDto.PreferredAuthEnum preferredAuthentication;
}
